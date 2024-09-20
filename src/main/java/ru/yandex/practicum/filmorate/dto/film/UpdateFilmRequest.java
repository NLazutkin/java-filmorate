package ru.yandex.practicum.filmorate.dto.film;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.DateAfterStandart;

import java.time.LocalDate;

@Data
public class UpdateFilmRequest {
    private Long id;
    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;
    @Size(max = 200, message = "Описание фильма не должно превышать 200 символов")
    private String description;
    @DateAfterStandart(message = "Дата релиза фильма не может быть раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательным числом")
    private Long duration;

    public boolean hasDescription() {
        return !StringUtils.isBlank(this.description);
    }

    public boolean hasReleaseDate() {
        return this.releaseDate != null;
    }

    public boolean hasDuration() {
        return this.duration != null;
    }
}
