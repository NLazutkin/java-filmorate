package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
class ReviewValidationTest {
    Review review;
    Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        review = new Review();
    }

    @Test
    void setReviewWithCorrectData() {
        review.setReviewId(1L);
        review.setUserId(1L);
        review.setFilmId(1L);
        review.setContent("Отзыв");
        review.setIsPositive(Boolean.TRUE);
        review.setUseful(10);

        Set<ConstraintViolation<Review>> violations = validator.validate(review);

        assertEquals(0, violations.size());
        assertTrue(violations.isEmpty(), "Ошибка валидации при заполнении класса Review корректными данными");
    }

    @Test
    void setReviewWithEmptyData() {
        Set<ConstraintViolation<Review>> violations = validator.validate(review);

        assertEquals(4, violations.size());
        assertFalse(violations.isEmpty(), "Ошибка валидации при заполнении класса Review пустыми данными");
    }

    @Test
    void setReviewWithEmptyUserId() {
        review.setReviewId(1L);
        review.setFilmId(1L);
        review.setContent("Отзыв");
        review.setIsPositive(Boolean.TRUE);
        review.setUseful(10);

        Set<ConstraintViolation<Review>> violations = validator.validate(review);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setReviewWithEmptyFilmId() {
        review.setReviewId(1L);
        review.setUserId(1L);
        review.setContent("Отзыв");
        review.setIsPositive(Boolean.TRUE);
        review.setUseful(10);

        Set<ConstraintViolation<Review>> violations = validator.validate(review);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setReviewWithEmptyContent() {
        review.setReviewId(1L);
        review.setUserId(1L);
        review.setFilmId(1L);
        review.setIsPositive(Boolean.TRUE);
        review.setUseful(10);

        Set<ConstraintViolation<Review>> violations = validator.validate(review);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setReviewWithEmptyIsPositive() {
        review.setReviewId(1L);
        review.setUserId(1L);
        review.setFilmId(1L);
        review.setContent("Отзыв");
        review.setUseful(10);

        Set<ConstraintViolation<Review>> violations = validator.validate(review);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setReviewWithEmptyUseful() {
        review.setReviewId(1L);
        review.setUserId(1L);
        review.setFilmId(1L);
        review.setContent("Отзыв");
        review.setIsPositive(Boolean.TRUE);

        Set<ConstraintViolation<Review>> violations = validator.validate(review);

        assertEquals(0, violations.size());
        assertTrue(violations.isEmpty(), "Ошибка валидации при не заполненном рейтинге отзыва");
    }
}
