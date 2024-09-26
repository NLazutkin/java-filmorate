package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.genre.NewGenreRequest;
import ru.yandex.practicum.filmorate.dto.genre.UpdateGenreRequest;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;
    private final String genrePath = "/{id}";

    @GetMapping(genrePath)
    public GenreDto findGenre(@PathVariable("id") Long genreId) {
        return genreService.findGenre(genreId);
    }

    @GetMapping
    public Collection<GenreDto> findAll() {
        return genreService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreDto create(@Valid @RequestBody NewGenreRequest genre) {
        return genreService.create(genre);
    }

    @PutMapping(genrePath)
    public GenreDto update(@Valid @RequestBody UpdateGenreRequest newGenre) {
        return genreService.update(newGenre);
    }

    @DeleteMapping(genrePath)
    public boolean delete(@PathVariable("id") Long mpaId) {
        return genreService.delete(mpaId);
    }
}
