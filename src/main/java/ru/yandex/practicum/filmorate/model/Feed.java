package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.enums.query.EventType;
import ru.yandex.practicum.filmorate.enums.query.OperationType;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Feed {
    Long eventId;
    Long userId;
    Long timestamp;
    EventType eventType;
    OperationType operation;
    Long entityId;
}
