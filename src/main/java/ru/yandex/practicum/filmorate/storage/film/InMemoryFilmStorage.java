package ru.yandex.practicum.filmorate.storage.film;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

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
        if (film.getLikes().contains(user.getId())) {
            throw new DuplicatedDataException(String.format("Пользователь %s уже ставил лайк фильму %s",
                    user.getName(), film.getName()));
        }

        film.getLikes().add(user.getId());
    }

    public LinkedHashSet<Long> getLikes(Long filmId) {
        Film film = films.get(filmId);
        LinkedHashSet<Long> likes = film.getLikes();

        if (likes == null || likes.isEmpty()) {
            log.trace(String.format("У фильма %s нет лайков", film.getName()));
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
    public void addGenreId(Genre genre, Film film) {
        Optional<LinkedHashSet<Long>> filmGenresIds = Optional.ofNullable(filmsGenresIds.get(film.getId()));
        LinkedHashSet<Long> genresIds = new LinkedHashSet<>();
        if (filmGenresIds.isPresent()) {
            genresIds = filmGenresIds.get();
        }
        genresIds.add(genre.getId());
        filmsGenresIds.put(film.getId(), genresIds);
    }

    @Override
    public void addDirectorId(Director director, Film film) {
        Optional<LinkedHashSet<Long>> filmDirectorsIds = Optional.ofNullable(filmsDirectorsIds.get(film.getId()));
        LinkedHashSet<Long> directorsIds = new LinkedHashSet<>();
        if (filmDirectorsIds.isPresent()) {
            directorsIds = filmDirectorsIds.get();
        }
        directorsIds.add(director.getId());
        filmsDirectorsIds.put(film.getId(), directorsIds);
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
        log.trace(String.format("Данные о фильме %s обновлены!", newFilm.getName()));
        return newFilm;
    }

    @Override
    public boolean delete(Long filmId) {
        films.remove(filmId);
        return Optional.ofNullable(films.get(filmId)).isPresent();
    }

    @Override
    public Collection<Film> searchFilms(String query, boolean byTitle, boolean byDirector) {
        String lowerQuery = query.toLowerCase();

        return films.values().stream()
                .filter(film -> {
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
                            if (director != null && director.getName().toLowerCase().contains(lowerQuery)) {
                                matchesDirector = true;
                                break;
                            }
                        }
                    }

                    return matchesTitle || matchesDirector;
                })
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size(), Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public Collection<Film> getRecommendedFilms(Long userId) {
        // фильмы, лайкнутые целевым юзером
        Set<Long> likedFilmsByUser = films.values().stream()
                .filter(film -> film.getLikes().contains(userId))
                .map(Film::getId)
                .collect(Collectors.toSet());

        // юзер с наибольшим кол-вом пересечений
        Long mostSimilarUserId = films.values().stream()
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
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);

        if (mostSimilarUserId == null) {
            return Collections.emptyList();
        }

        return films.values().stream()
                .filter(film -> film.getLikes().contains(mostSimilarUserId) && !likedFilmsByUser.contains(film.getId()))
                .collect(Collectors.toList());
    }
}
