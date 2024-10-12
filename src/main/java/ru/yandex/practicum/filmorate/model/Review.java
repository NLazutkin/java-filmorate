package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"reviewId"})
public class Review {
    Long reviewId;
    @NotNull(message = "У отзыва должен быть автор. Укажите пользователя")
    Long userId;
    @NotNull(message = "Отзыв должен быть написан для фильма. Укажите фильм")
    Long filmId;
    @NotNull(message = "Отзыв должен содержать текст. Напишите отзыв!")
    String content;
    @NotNull(message = "Тип отзыва не должен быть пустым")
    Boolean isPositive;
    Integer useful = 0;

    public void increaseUseful() {
        this.useful++;
    }

    public void decreaseUseful() {
        this.useful--;
    }
}