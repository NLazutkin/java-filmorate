package ru.yandex.practicum.filmorate.dto.mpa;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewMpaRequest {
    @NotNull(message = "Название рейтинга не должно быть пустым")
    String name;
    String description;
}
