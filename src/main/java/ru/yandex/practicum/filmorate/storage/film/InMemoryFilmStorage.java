package ru.yandex.practicum.filmorate.storage.film;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.director.InMemoryDirectorStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("InMemoryFilmStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InMemoryFilmStorage implements FilmStorage {
    Map<Long, Film> films = new HashMap<>();
    Map<Long, LinkedHashSet<Long>> filmsGenresIds = new HashMap<>();
    Map<Long, LinkedHashSet<Long>> filmsDirectorsIds = new HashMap<>();
    Map<Long, Long> filmsMpaId = new HashMap<>();

    InMemoryDirectorStorage directorStorage;

    @Autowired
    public InMemoryFilmStorage(@Lazy InMemoryDirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    // вспомогательный метод  генерации идентификатора нового  поста
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findFilm(Long filmId) {
        return Optional.ofNullable(films.get(filmId))
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с ID %d не найден", filmId)));
    }

    @Override
    public Collection<Film> findPopular(Integer count) {
        return findAll()
                .stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Film> findPopularByYear(Integer count, Integer year) {
        return findAll()
                .stream()
                .filter(film -> film.getReleaseDate().getYear() == year)
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Film> findPopularByGenre(Integer count, Long genreId) {
        return findAll()
                .stream()
                .filter(film -> film.getGenres().stream().anyMatch(genre -> genre.getId().equals(genreId)))
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Film> findPopularByGenreAndYear(Integer count, Long genreId, Integer year) {
        return findAll()
                .stream()
                .filter(film -> film.getReleaseDate().getYear() == year)
                .filter(film -> film.getGenres().stream().anyMatch(genre -> genre.getId().equals(genreId)))
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Film> findDirectorFilms(Long directorId) {
        return filmsDirectorsIds.entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(directorId))
                .map(Map.Entry::getKey)
                .map(films::get)
                .toList();
    }

    @Override
    public Collection<Film> findDirectorFilmsOrderYear(Long directorId) {
        return filmsDirectorsIds.entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(directorId))
                .map(Map.Entry::getKey)
                .map(films::get)
                .sorted(Comparator.comparing(Film::getReleaseDate, Comparator.naturalOrder()))
                .toList();
    }

    @Override
    public Collection<Film> findDirectorFilmsOrderLikes(Long directorId) {
        return filmsDirectorsIds.entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(directorId))
                .map(Map.Entry::getKey)
                .map(films::get)
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size(), Comparator.reverseOrder()))
                .toList();
    }

    @Override
    public void addLike(Film film, User user) {
        film.getLikes().add(user.getId());
    }

    @Override
    public LinkedHashSet<Long> getLikes(Long filmId) {
        Film film = films.get(filmId);
        LinkedHashSet<Long> likes = film.getLikes();

        if (likes == null || likes.isEmpty()) {
            log.trace("У фильма {} нет лайков", film.getName());
            return new LinkedHashSet<>();
        }

        return likes;
    }

    @Override
    public Collection<Film> findUserFilms(Long userId) {
        return findAll().stream()
                .filter(film -> film.getLikes().contains(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLike(Film film, User user) {
        if (!film.getLikes().contains(user.getId())) {
            throw new NotFoundException(String.format("Пользователь %s не ставил лайк фильму %s. Удалить лайк невозможно",
                    user.getName(), film.getName()));
        }

        film.getLikes().remove(user.getId());
    }

    @Override
    public void addGenreId(Long genreId, Long filmId) {
        Optional<LinkedHashSet<Long>> filmGenresIds = Optional.ofNullable(filmsGenresIds.get(filmId));
        LinkedHashSet<Long> genresIds = new LinkedHashSet<>();
        if (filmGenresIds.isPresent()) {
            genresIds = filmGenresIds.get();
        }
        genresIds.add(genreId);
        filmsGenresIds.put(filmId, genresIds);
    }

    @Override
    public void addDirectorId(Long directorId, Long filmId) {
        Optional<LinkedHashSet<Long>> filmDirectorsIds = Optional.ofNullable(filmsDirectorsIds.get(filmId));
        LinkedHashSet<Long> directorsIds = new LinkedHashSet<>();
        if (filmDirectorsIds.isPresent()) {
            directorsIds = filmDirectorsIds.get();
        }
        directorsIds.add(directorId);
        filmsDirectorsIds.put(filmId, directorsIds);
    }

    @Override
    public boolean deleteGenreIds(Long filmId) {
        return Optional.ofNullable(filmsGenresIds.remove(filmId)).isPresent();
    }

    @Override
    public boolean deleteDirectorIds(Long filmId) {
        return Optional.ofNullable(filmsDirectorsIds.remove(filmId)).isPresent();
    }

    @Override
    public boolean deleteGenreIds(Long filmId, Long genreId) {
        return filmsGenresIds.get(filmId).remove(genreId);
    }

    @Override
    public boolean deleteDirectorIds(Long filmId, Long directorId) {
        return filmsDirectorsIds.get(filmId).remove(directorId);
    }

    @Override
    public LinkedHashSet<Long> findGenresIds(Long filmId) {
        LinkedHashSet<Long> genresIds = filmsGenresIds.get(filmId);
        if (genresIds == null || genresIds.isEmpty()) {
            return new LinkedHashSet<>();
        }
        return genresIds;
    }

    @Override
    public LinkedHashSet<Long> findDirectorsIds(Long filmId) {
        LinkedHashSet<Long> directorsIds = filmsDirectorsIds.get(filmId);
        if (directorsIds == null || directorsIds.isEmpty()) {
            return new LinkedHashSet<>();
        }
        return directorsIds;
    }

    @Override
    public Long findRatingId(Long filmId) {
        return Optional.ofNullable(filmsMpaId.get(filmId))
                .orElseThrow(() -> new NotFoundException(String.format("Рейтинг для фильма с ID %d не найден", filmId)));
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);

        filmsMpaId.put(film.getId(), film.getMpa().getId());
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        films.put(newFilm.getId(), newFilm);
        filmsMpaId.put(newFilm.getId(), newFilm.getMpa().getId());
        log.trace("Данные о фильме {} обновлены!", newFilm.getName());
        return newFilm;
    }

    @Override
    public boolean delete(Long filmId) {
        films.remove(filmId);
        return Optional.ofNullable(films.get(filmId)).isPresent();
    }

    @Override
    public Collection<Film> getRecommendedFilms(Long userId) {
        Set<Long> filmsLikedByUser = findUserFilms(userId).stream()
                .map(Film::getId)
                .collect(Collectors.toSet());

        Long mostSimilarUserId = findMostSimilarUser(userId, filmsLikedByUser);

        if (mostSimilarUserId == null || filmsLikedByUser.isEmpty()) {
            return Collections.emptyList();
        }

        return getFilmsLikedBySimilarUser(mostSimilarUserId, filmsLikedByUser);
    }

    @Override
    public Collection<Film> searchFilms(String query, boolean byTitle, boolean byDirector) {
        String lowerQuery = query.toLowerCase();

        return films.values().stream()
                .filter(film -> searchFilter(film, lowerQuery, byTitle, byDirector))
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size(), Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    // методы для работы с памятью
    private Long findMostSimilarUser(Long userId, Set<Long> likedFilmsByUser) {
        return films.values().stream()
                .flatMap(film -> film.getLikes().stream())
                .filter(otherUserId -> !otherUserId.equals(userId))
                .distinct()
                .map(otherUserId -> Map.entry(
                        otherUserId,
                        films.values().stream()
                                .filter(film -> film.getLikes().contains(otherUserId)
                                        && likedFilmsByUser.contains(film.getId()))
                                .count()
                ))
                .filter(pair -> pair.getValue() > 0)
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private Collection<Film> getFilmsLikedBySimilarUser(Long mostSimilarUserId, Set<Long> likedFilmsByUser) {
        return films.values().stream()
                .filter(film -> film.getLikes().contains(mostSimilarUserId) && !likedFilmsByUser.contains(film.getId()))
                .collect(Collectors.toList());
    }

    private boolean searchFilter(Film film, String lowerQuery, boolean byTitle, boolean byDirector) {
        boolean matchesTitle = false;
        boolean matchesDirector = false;

        if (byTitle) {
            matchesTitle = film.getName().toLowerCase().contains(lowerQuery);
        }

        if (byDirector) {
            LinkedHashSet<Long> directorIds = findDirectorsIds(film.getId());
            for (Long directorId : directorIds) {
                Director director = film.getDirectors().stream()
                        .filter(d -> d.getId().equals(directorId))
                        .findFirst()
                        .orElse(null);
                if (director != null
                        && directorStorage.findDirector(director.getId()).getName().toLowerCase().contains(lowerQuery)) {
                    matchesDirector = true;
                    break;
                }
            }
        }

        return matchesTitle || matchesDirector;
    }

    public void deleteDirectorIdsByDirector(Long directorId) {
        filmsDirectorsIds.values().forEach(element -> element.remove(directorId));
    }
}
