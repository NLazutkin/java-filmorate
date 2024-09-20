package ru.yandex.practicum.filmorate.dto.mpa;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateMpaRequest {
    private Long id;
    @NotNull(message = "Название рейтинга не должно быть пустым")
    private String name;
    private String description;

    public boolean hasDescription() {
        return !StringUtils.isBlank(this.description);
    }
}
