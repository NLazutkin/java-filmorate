package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.LinkedHashSet;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {

    public static FilmDto mapToFilmDto(Film film, Mpa mpa,
                                       LinkedHashSet<Genre> genres,
                                       LinkedHashSet<Long> likes,
                                       LinkedHashSet<Director> directors) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setLikes(likes);
        dto.setGenres(genres);
        dto.setDirectors(directors);
        dto.setMpa(MpaMapper.mapToMpaDto(mpa));

        return dto;
    }

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setLikes(film.getLikes());
        dto.setGenres(film.getGenres());
        dto.setDirectors(film.getDirectors());
        dto.setMpa(MpaMapper.mapToMpaDto(film.getMpa()));

        return dto;
    }

    public static Film mapToFilm(NewFilmRequest request) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());
        film.setLikes(request.getLikes());
        film.setGenres(request.getGenres());
        film.setDirectors(request.getDirectors());

        Optional<Mpa> mpa = Optional.ofNullable(request.getMpa());
        if (mpa.isEmpty()) {
            film.setMpa(new Mpa());
        } else {
            film.setMpa(request.getMpa());
        }

        return film;
    }

    public static Film updateFilmFields(Film film, UpdateFilmRequest request) {
        film.setName(request.getName());

        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        }

        if (request.hasReleaseDate()) {
            film.setReleaseDate(request.getReleaseDate());
        }

        if (request.hasDuration()) {
            film.setDuration(request.getDuration());
        }

        if (request.hasLikes()) {
            film.setLikes(request.getLikes());
        }

        if (request.hasGenres()) {
            film.setGenres(request.getGenres());
        }

        if (request.hasDirectors()) {
            film.setDirectors(request.getDirectors());
        }

        if (request.hasMpa()) {
            film.setMpa(request.getMpa());
        }

        return film;
    }
}