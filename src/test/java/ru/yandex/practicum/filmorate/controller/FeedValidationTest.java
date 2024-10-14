package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.enums.actions.EventType;
import ru.yandex.practicum.filmorate.enums.actions.OperationType;
import ru.yandex.practicum.filmorate.model.Feed;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)

public class FeedValidationTest {
    Feed feed;
    Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        feed = new Feed();
    }

    @Test
    void setFeedWithCorrectData() {
        feed.setEventId(1L);
        feed.setUserId(1L);
        feed.setTimestamp(System.currentTimeMillis());
        feed.setEventType(EventType.FRIEND);
        feed.setOperation(OperationType.ADD);
        feed.setEntityId(1L);

        Set<ConstraintViolation<Feed>> violations = validator.validate(feed);

        assertEquals(0, violations.size());
        assertTrue(violations.isEmpty(), "Ошибка валидации при заполнении класса Film корректными данными");
    }

    @Test
    void setFeedWithEmptyData() {
        Set<ConstraintViolation<Feed>> violations = validator.validate(feed);

        assertEquals(5, violations.size());
        assertFalse(violations.isEmpty(), "Ошибка валидации при заполнении класса Film пустыми данными");
    }

    @Test
    void setFeedWithNotPositiveIds() {
        feed.setEventId(1L);
        feed.setUserId(-1L);
        feed.setTimestamp(System.currentTimeMillis());
        feed.setEventType(EventType.FRIEND);
        feed.setOperation(OperationType.ADD);
        feed.setEntityId(-1L);

        Set<ConstraintViolation<Feed>> violations = validator.validate(feed);

        assertEquals(2, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setFeedWithEmptyIds() {
        feed.setEventId(1L);
        feed.setUserId(null);
        feed.setTimestamp(System.currentTimeMillis());
        feed.setEventType(EventType.FRIEND);
        feed.setOperation(OperationType.ADD);
        feed.setEntityId(null);

        Set<ConstraintViolation<Feed>> violations = validator.validate(feed);

        assertEquals(2, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setFeedWithTimestampInFuture() {
        feed.setEventId(1L);
        feed.setUserId(1L);
        feed.setEventType(EventType.FRIEND);
        feed.setOperation(OperationType.ADD);
        feed.setEntityId(1L);

        Set<ConstraintViolation<Feed>> violations = validator.validate(feed);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setFeedWithOutEventType() {
        feed.setEventId(1L);
        feed.setUserId(1L);
        feed.setTimestamp(System.currentTimeMillis());
        feed.setOperation(OperationType.ADD);
        feed.setEntityId(1L);

        Set<ConstraintViolation<Feed>> violations = validator.validate(feed);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setFeedWithOutOperation() {
        feed.setEventId(1L);
        feed.setUserId(1L);
        feed.setTimestamp(System.currentTimeMillis());
        feed.setEventType(EventType.FRIEND);
        feed.setEntityId(1L);

        Set<ConstraintViolation<Feed>> violations = validator.validate(feed);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }
}
