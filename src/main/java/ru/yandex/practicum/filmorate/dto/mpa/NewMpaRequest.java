package ru.yandex.practicum.filmorate.dto.mpa;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class NewMpaRequest {
    @NotNull(message = "Название рейтинга не должно быть пустым")
    private String name;
    private String description;
}
