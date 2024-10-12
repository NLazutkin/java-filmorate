package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.dto.mpa.NewMpaRequest;
import ru.yandex.practicum.filmorate.dto.mpa.UpdateMpaRequest;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MpaController {
    MpaService mpaService;
    String mpaPath = "/{id}";

    @GetMapping(mpaPath)
    public MpaDto findMpa(@PathVariable("id") Long mpaId) {
        return mpaService.findMpa(mpaId);
    }

    @GetMapping
    public Collection<MpaDto> findAll() {
        return mpaService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MpaDto create(@Valid @RequestBody NewMpaRequest mpa) {
        return mpaService.create(mpa);
    }

    @PutMapping(mpaPath)
    public MpaDto update(@Valid @RequestBody UpdateMpaRequest newMpa) {
        return mpaService.update(newMpa);
    }

    @DeleteMapping(mpaPath)
    public boolean delete(@PathVariable("id") Long mpaId) {
        return mpaService.delete(mpaId);
    }
}
