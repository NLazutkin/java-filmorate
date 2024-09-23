package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GenreValidationTest {
    private Genre genre;
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        genre = new Genre();
    }

    @Test
    void setMpaWithCorrectData() {
        genre.setId(1L);
        genre.setName("Комедия");

        Set<ConstraintViolation<Genre>> violations = validator.validate(genre);

        assertEquals(0, violations.size());
        assertTrue(violations.isEmpty(), "Ошибка валидации при заполнении класса Genre");
    }

    @Test
    void setMpaWithEmptyData() {
        Set<ConstraintViolation<Genre>> violations = validator.validate(genre);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), "Ошибка валидации при заполнении класса Genre пустыми данными");
    }
}
