package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Collection<Film> findAll() {
        return films.values();
    }

    public Optional<Film> findFilm(Long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.trace("Фильм " + film.getName() + " сохранен!");
        return film;
    }

    public Film update(Film newFilm) {
        films.put(newFilm.getId(), newFilm);
        log.trace("Данные о фильме " + newFilm.getName() + " обновлены!");
        return newFilm;
    }
}
