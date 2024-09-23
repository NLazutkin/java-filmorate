package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String name;
    String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDate releaseDate;
    Long duration;
    LinkedHashSet<Long> likes = new LinkedHashSet<>();
    LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    MpaDto mpa;
}
