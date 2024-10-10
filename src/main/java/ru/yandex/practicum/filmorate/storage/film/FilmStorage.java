package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.LinkedHashSet;

public interface FilmStorage {
    Film findFilm(Long filmId);

    Film update(Film newFilm);

    Film create(Film film);

    Collection<Film> findAll();

    Collection<Film> findPopular(Integer count);

    Collection<Film> findDirectorFilms(Long directorId);

    Collection<Film> findDirectorFilmsOrderYear(Long directorId);

    Collection<Film> findDirectorFilmsOrderLikes(Long directorId);

    boolean delete(Long filmId);

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);

    void addGenreId(Genre genre, Film film);

    void addDirectorId(Director director, Film film);

    LinkedHashSet<Long> findGenresIds(Long filmId);

    LinkedHashSet<Long> findDirectorsIds(Long filmId);

    Long findRatingId(Long filmId);

    LinkedHashSet<Long> getLikes(Long filmId);

    Collection<Film> findUserFilms(Long userId);

    Collection<Film> searchFilms(String query, boolean byTitle, boolean byDirector);
}