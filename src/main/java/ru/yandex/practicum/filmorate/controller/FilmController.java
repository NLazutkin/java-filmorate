package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.*;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private final String filmLikePath = "/{id}/like/{user-id}";

    @PutMapping(filmLikePath)
    public void addLike(@PathVariable("id") Long filmId, @PathVariable("user-id") Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping(filmLikePath)
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable("user-id") Long userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> findPopular(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        return filmService.findPopular(count);
    }

    @GetMapping("/{id}")
    public FilmDto findFilm(@PathVariable("id") Long filmId) {
        return filmService.findFilm(filmId);
    }

    @GetMapping
    public Collection<FilmDto> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto create(@Valid @RequestBody NewFilmRequest film) {
        return filmService.create(film);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody UpdateFilmRequest newFilm) {
        return filmService.update(newFilm);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long filmId) {
        return filmService.delete(filmId);
    }

    @GetMapping("/director/{directorId}")
    public Collection<FilmDto> findDirectorFilms(@PathVariable("directorId") Long directorId,
                                                 @RequestParam(name = "sortBy", defaultValue = "") String sortConditions) {
        return filmService.findDirectorFilms(directorId, sortConditions);
    }

    @GetMapping("/search")
    public Collection<FilmDto> searchFilms(@RequestParam String query,
                                           @RequestParam String by) {
        log.debug("Запрос на поиск фильмов: query='{}', by='{}'", query, by);
        return filmService.searchFilms(query, by);
    }

    @GetMapping("/common")
    public Collection<FilmDto> findCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        return filmService.findCommonFilms(userId, friendId);
    }
}
