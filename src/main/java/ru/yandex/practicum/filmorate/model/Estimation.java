package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"reviewId", "userId"})
public class Estimation {
    @NotNull(message = "Оценка должна быть поставлена отзыву. Добавьте id отзыва")
    Long reviewId;
    @NotNull(message = "Оценка должна быть поставлена отзыву. Добавьте id отзыва")
    Long userId;
    @NotNull(message = "Оценка должна быть положительной или отрицательной. Добавьте \"true\" или \"false\"")
    Boolean isLike;
}
