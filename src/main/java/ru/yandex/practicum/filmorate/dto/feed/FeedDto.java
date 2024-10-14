package ru.yandex.practicum.filmorate.dto.feed;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.enums.actions.EventType;
import ru.yandex.practicum.filmorate.enums.actions.OperationType;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long eventId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long userId;
    Long timestamp;
    EventType eventType;
    OperationType operation;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long entityId;
}
