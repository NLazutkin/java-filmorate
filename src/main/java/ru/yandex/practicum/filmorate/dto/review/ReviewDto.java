package ru.yandex.practicum.filmorate.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long reviewId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long userId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long filmId;
    String content;
    Boolean isPositive;
    Integer useful = 0;
}

