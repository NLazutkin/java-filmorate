package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private final LocalDate cinemaBirthDay = LocalDate.of(1895, 12, 28);

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Создаем запись о фильме");
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.trace("Фильм \n" + film + "\n сохранен!");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Обновляем данные фильма");

        if (newFilm.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }

        if (films.containsKey(newFilm.getId())) {
            films.put(newFilm.getId(), newFilm);
            log.trace("Фильм \n" + newFilm + "\n обновлен!");
            return newFilm;
        }

        log.error("Фильм с id = " + newFilm.getId() + " под названием \"" + newFilm.getName() + "\" не найден");
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " под названием \"" + newFilm.getName() + "\" не найден");
    }
}
