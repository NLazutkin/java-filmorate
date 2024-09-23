package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.annotation.DateAfterStandart;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id"})
public class Film {
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
    Mpa mpa;
}
