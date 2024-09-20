package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {
    Film findFilm(Long filmId);

    Film update(Film newFilm);

    Film create(Film film);

    Collection<Film> findAll();

    Collection<Film> findPopular(Integer count);

    boolean delete(Long filmId);

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);

    void addGenre(Genre genre, Film film);

    Set<Genre> findGenresById(Long filmId);

    Mpa findRatingById(Long filmId);
}
