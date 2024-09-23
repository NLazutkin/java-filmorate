package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.DateAfterStandart;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
public class NewFilmRequest {
    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;
    @Size(max = 200, message = "Описание фильма не должно превышать 200 символов")
    private String description;
    @DateAfterStandart(message = "Дата релиза фильма не может быть раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательным числом")
    private Long duration;
    private LinkedHashSet<Long> likes = new LinkedHashSet<>();
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    private Mpa mpa;
}
