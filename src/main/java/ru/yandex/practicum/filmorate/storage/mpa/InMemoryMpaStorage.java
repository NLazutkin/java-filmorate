package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Slf4j
@Component("InMemoryMpaStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InMemoryMpaStorage implements MpaStorage {
    Map<Long, Mpa> mpas = new HashMap<>();

    public InMemoryMpaStorage() {
        mpas.put(1L, new Mpa(1L, "G", "У фильма нет возрастных ограничений"));
        mpas.put(2L, new Mpa(2L, "PG", "Детям рекомендуется смотреть фильм с родителямий"));
        mpas.put(3L, new Mpa(3L, "PG-13", "У фильма нет возрастных ограничений"));
        mpas.put(4L, new Mpa(4L, "R", "Лицам до 17 лет просматривать фильм можно только в присутствии взрослого"));
        mpas.put(5L, new Mpa(5L, "NC-17", "Лицам до 18 лет просмотр запрещён"));
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = mpas.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public boolean isMpaWithSameNameExist(String name) {
        return mpas.values().stream().anyMatch(mpaFromMemory -> mpaFromMemory.getName().equals(name));
    }

    @Override
    public Collection<Mpa> findAll() {
        return mpas.values();
    }

    @Override
    public Mpa findMpa(Mpa mpa) {
        return Optional.ofNullable(mpas.get(mpa.getId()))
                .orElseThrow(() -> new MpaNotFoundException(String.format("В запросе не корректный рейтинг MPA с ID %d", mpa.getId())));
    }


    @Override
    public Mpa findMpa(Long mpaId) {
        return Optional.ofNullable(mpas.get(mpaId))
                .orElseThrow(() -> new NotFoundException(String.format("Рейтинг MPA с ID %d не найден", mpaId)));
    }

    @Override
    public Mpa create(Mpa mpa) {
        mpa.setId(getNextId());
        mpas.put(mpa.getId(), mpa);
        return mpa;
    }

    @Override
    public Mpa update(Mpa newMpa) {
        mpas.put(newMpa.getId(), newMpa);
        log.trace("Данные о рейтинге {} обновлены!", newMpa.getName());
        return newMpa;
    }

    @Override
    public boolean delete(Long filmId) {
        mpas.remove(filmId);
        return Optional.ofNullable(mpas.get(filmId)).isPresent();
    }
}
