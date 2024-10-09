package ru.yandex.practicum.filmorate.dto.review;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateReviewRequest {
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

    public boolean hasContent() {
        return !StringUtils.isBlank(this.content);
    }

    public boolean hasUseful() {
        return this.useful != null;
    }
}
