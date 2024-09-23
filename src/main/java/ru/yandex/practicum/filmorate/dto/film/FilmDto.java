package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate releaseDate;
    private Long duration;
    private LinkedHashSet<Long> likes = new LinkedHashSet<>();
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    private MpaDto mpa;
}
