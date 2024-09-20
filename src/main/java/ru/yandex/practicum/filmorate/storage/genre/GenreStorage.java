package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {
    Genre findGenre(Long genreId);

    Genre findGenre(Genre genre);

    Collection<Genre> findAll();

    Genre create(Genre genre);

    Genre update(Genre genre);

    boolean delete(Long id);

    boolean isGenreWithSameNameExist(String name);
}