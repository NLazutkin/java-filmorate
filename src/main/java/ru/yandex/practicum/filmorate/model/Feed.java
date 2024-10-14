package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.enums.actions.EventType;
import ru.yandex.practicum.filmorate.enums.actions.OperationType;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"eventId"})
public class Feed {
    Long eventId;
    @NotNull(message = "У события должен быть источник. Укажите пользователя")
    @Positive(message = "Id пользователя не может быть меньше 0")
    Long userId;
    @NotNull(message = "У события должна быть временная метка")
    Long timestamp;
    @NotNull(message = "У события должен быть тип. Укажите тип LIKE, REVIEW или FRIEND")
    EventType eventType;
    @NotNull(message = "У события должен быть тип операции. Укажите тип ADD,REMOVE или UPDATE")
    OperationType operation;
    @NotNull(message = "У события должна быть сущность. Лайк/дизлайк, добавление в друзья или отзыв ")
    @Positive(message = "Id сущности не может быть меньше 0")
    Long entityId;
}
