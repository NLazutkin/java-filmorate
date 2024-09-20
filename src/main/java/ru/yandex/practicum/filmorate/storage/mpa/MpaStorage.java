package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaStorage {
    Mpa findMpa(Long userId);

    Mpa findMpa(Mpa mpa);

    Collection<Mpa> findAll();

    Mpa create(Mpa mpa);

    Mpa update(Mpa mpa);

    boolean delete(Long id);

    boolean isMpaWithSameNameExist(String name);
}