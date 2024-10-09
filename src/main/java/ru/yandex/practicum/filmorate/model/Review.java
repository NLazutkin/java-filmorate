package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id"})
public class Review {
    Long id;
    @NotNull(message = "У отзыва должен быть автор. Укажите пользователя")
    Long user_id;
    @NotNull(message = "Отзыв должен быть написан для фильма. Укажите фильм")
    Long film_id;
    String content;
    @NotNull
    @Pattern(regexp = "^true$|^false$", message = "Может быть только true или false")
    boolean isPositive;
    Integer useful = 0;
}
