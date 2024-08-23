package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> findFilm(Long filmId);

    Film update(Film newFilm);

    Film create(Film film);

    Collection<Film> findAll();
}
