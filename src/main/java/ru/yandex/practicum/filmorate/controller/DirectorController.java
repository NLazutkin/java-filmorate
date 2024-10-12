package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.dto.director.NewDirectorRequest;
import ru.yandex.practicum.filmorate.dto.director.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DirectorController {
    DirectorService directorService;
    String directorPath = "/{id}";

    @GetMapping(directorPath)
    public DirectorDto findDirector(@PathVariable("id") Long genreId) {
        return directorService.findDirector(genreId);
    }

    @GetMapping
    public Collection<DirectorDto> findAll() {
        return directorService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DirectorDto create(@Valid @RequestBody NewDirectorRequest genre) {
        return directorService.create(genre);
    }

    @PutMapping
    public DirectorDto update(@Valid @RequestBody UpdateDirectorRequest newGenre) {
        return directorService.update(newGenre);
    }

    @DeleteMapping(directorPath)
    public boolean delete(@PathVariable("id") Long mpaId) {
        return directorService.delete(mpaId);
    }
}
