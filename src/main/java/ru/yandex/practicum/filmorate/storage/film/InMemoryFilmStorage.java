/*
package ru.yandex.practicum.filmorate.storage.film;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

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
    Map<Long, Long> filmsMpaId = new HashMap<>();

    // вспомогательный метод для генерации идентификатора нового поста
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
    public LinkedHashSet<Long> findGenresIds(Long filmId) {
        LinkedHashSet<Long> genresIds = filmsGenresIds.get(filmId);
        if (genresIds == null || genresIds.isEmpty()) {
            return new LinkedHashSet<>();
        }
        return genresIds;
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
        log.trace(String.format("Данные о фильме %s обновлены!",  newFilm.getName()));
        return newFilm;
    }

    @Override
    public boolean delete(Long filmId) {
        films.remove(filmId);
        return Optional.ofNullable(films.get(filmId)).isPresent();
    }
}
*/
