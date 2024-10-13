package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Estimation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
class EstimateValidationTest {
    Estimation estimation;
    Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        estimation = new Estimation();
    }

    @Test
    void setReviewWithCorrectData() {
        estimation.setReviewId(1L);
        estimation.setUserId(1L);
        estimation.setIsLike(Boolean.TRUE);

        Set<ConstraintViolation<Estimation>> violations = validator.validate(estimation);

        assertEquals(0, violations.size());
        assertTrue(violations.isEmpty(), "Ошибка валидации при заполнении класса Review корректными данными");
    }

    @Test
    void setReviewWithEmptyData() {
        Set<ConstraintViolation<Estimation>> violations = validator.validate(estimation);

        assertEquals(3, violations.size());
        assertFalse(violations.isEmpty(), "Ошибка валидации при заполнении класса Review пустыми данными");
    }

    @Test
    void setReviewWithEmptyReviewId() {
        estimation.setUserId(1L);
        estimation.setIsLike(Boolean.TRUE);
        Set<ConstraintViolation<Estimation>> violations = validator.validate(estimation);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setReviewWithEmptyUserId() {
        estimation.setReviewId(1L);
        estimation.setIsLike(Boolean.TRUE);

        Set<ConstraintViolation<Estimation>> violations = validator.validate(estimation);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setReviewWithEmptyIsLike() {
        estimation.setReviewId(1L);
        estimation.setUserId(1L);

        Set<ConstraintViolation<Estimation>> violations = validator.validate(estimation);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }
}
