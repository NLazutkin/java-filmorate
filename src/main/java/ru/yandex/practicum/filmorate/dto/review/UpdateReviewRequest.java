package ru.yandex.practicum.filmorate.dto.review;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateReviewRequest {
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

    public boolean hasContent() {
        return !StringUtils.isBlank(this.content);
    }

    public boolean hasUseful() {
        return this.useful != null;
    }
}
