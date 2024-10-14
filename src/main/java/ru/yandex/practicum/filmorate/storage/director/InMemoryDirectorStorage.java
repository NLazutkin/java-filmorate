package ru.yandex.practicum.filmorate.storage.director;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component("InMemoryDirectorStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InMemoryDirectorStorage implements DirectorStorage {
    Map<Long, Director> directors = new HashMap<>();
    InMemoryFilmStorage filmStorage;

    @Autowired
    public InMemoryDirectorStorage(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = directors.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public boolean isDirectorWithSameNameExist(String name) {
        return directors.values().stream().anyMatch(directorFromMemory -> directorFromMemory.getName().equals(name));
    }

    @Override
    public Collection<Director> findAll() {
        return directors.values();
    }

    @Override
    public Director findDirector(Director director) {
        return Optional.ofNullable(directors.get(director.getId()))
                .orElseThrow(() -> new DirectorNotFoundException(String.format("В запросе не корректный режиссер с ID %d", director.getId())));
    }

    @Override
    public Director findDirector(Long directorId) {
        return Optional.ofNullable(directors.get(directorId))
                .orElseThrow(() -> new NotFoundException(String.format("Режиссер с ID %d не найден", directorId)));
    }

    @Override
    public Director create(Director director) {
        director.setId(getNextId());
        directors.put(director.getId(), director);
        return director;
    }

    @Override
    public Director update(Director newDirector) {
        directors.put(newDirector.getId(), newDirector);
        log.trace("Данные о режиссере {} обновлены!", newDirector.getName());
        return newDirector;
    }

    @Override
    public boolean delete(Long directorId) {
        directors.remove(directorId);
        filmStorage.deleteDirectorIdsByDirector(directorId);
        return Optional.ofNullable(directors.get(directorId)).isPresent();
    }
}
