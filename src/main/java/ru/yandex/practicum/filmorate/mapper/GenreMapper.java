package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.genre.NewGenreRequest;
import ru.yandex.practicum.filmorate.dto.genre.UpdateGenreRequest;
import ru.yandex.practicum.filmorate.model.Genre;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenreMapper {

    public static GenreDto mapToGenreDto(Genre genre) {
        GenreDto dto = new GenreDto();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }

    public static Genre mapToGenre(NewGenreRequest request) {
        Genre genre = new Genre();
        genre.setName(request.getName());

        return genre;
    }

    public static Genre updateGenreFields(Genre genre, UpdateGenreRequest request) {
        genre.setName(request.getName());

        return genre;
    }
}
