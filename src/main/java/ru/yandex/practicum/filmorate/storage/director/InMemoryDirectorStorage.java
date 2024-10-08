package ru.yandex.practicum.filmorate.storage.director;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component("InMemoryDirectorStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InMemoryDirectorStorage implements DirectorStorage {
    Map<Long, Director> directors = new HashMap<>();

    public InMemoryDirectorStorage() {
        directors.put(1L, new Director(1L, "Джордж Лукас"));
        directors.put(2L, new Director(2L, "Фрэнк Дарабонт"));
        directors.put(3L, new Director(3L, "Рассел Малкэй"));
        directors.put(4L, new Director(4L, "Пьер Коффан"));
        directors.put(5L, new Director(5L, "Крис Рено"));
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
        return directors.values().stream().anyMatch(genreFromMemory -> genreFromMemory.getName().equals(name));
    }

    @Override
    public Collection<Director> findAll() {
        return directors.values();
    }

    @Override
    public Director findDirector(Director director) {
        return Optional.ofNullable(directors.get(director.getId()))
                .orElseThrow(() -> new GenreNotFoundException(String.format("В запросе не корректный режиссер с ID %d", director.getId())));
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
        log.trace(String.format("Данные о режиссере %s обновлены!", newDirector.getName()));
        return newDirector;
    }

    @Override
    public boolean delete(Long directorId) {
        directors.remove(directorId);
        return Optional.ofNullable(directors.get(directorId)).isPresent();
    }
}
