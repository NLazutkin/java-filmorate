package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

public interface DirectorStorage {
    Director findDirector(Long directorId);

    Director findDirector(Director director);

    Collection<Director> findAll();

    Director create(Director director);

    Director update(Director director);

    boolean delete(Long directorId);

    boolean isDirectorWithSameNameExist(String name);
}

