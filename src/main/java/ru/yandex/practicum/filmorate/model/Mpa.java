package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {
    private Long id;
    @NotNull(message = "Название рейтинга не должно быть пустым")
    private String name;
    private String description;
}
