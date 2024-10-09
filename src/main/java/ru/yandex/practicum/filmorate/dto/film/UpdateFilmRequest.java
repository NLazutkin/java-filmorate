package ru.yandex.practicum.filmorate.dto.film;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.annotation.DateAfterStandart;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateFilmRequest {
    Long id;
    @NotBlank(message = "Название фильма не должно быть пустым")
    String name;
    @Size(max = 200, message = "Описание фильма не должно превышать 200 символов")
    String description;
    @DateAfterStandart(message = "Дата релиза фильма не может быть раньше 28 декабря 1895 года")
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательным числом")
    Long duration;
    LinkedHashSet<Long> likes = new LinkedHashSet<>();
    LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    LinkedHashSet<Director> directors = new LinkedHashSet<>();
    Mpa mpa;

    public boolean hasDescription() {
        return !StringUtils.isBlank(this.description);
    }

    public boolean hasReleaseDate() {
        return this.releaseDate != null;
    }

    public boolean hasDuration() {
        return this.duration != null;
    }

    public boolean hasLikes() {
        return this.likes != null;
    }

    public boolean hasGenres() {
        return this.genres != null;
    }

    public boolean hasDirectors() {
        return this.genres != null;
    }

    public boolean hasMpa() {
        return this.mpa != null;
    }
}
