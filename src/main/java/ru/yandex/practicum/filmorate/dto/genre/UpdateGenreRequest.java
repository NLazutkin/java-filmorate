package ru.yandex.practicum.filmorate.dto.genre;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateGenreRequest {
    @NotNull(message = "Название рейтинга не должно быть пустым")
    private Long id;
    private String name;
}
