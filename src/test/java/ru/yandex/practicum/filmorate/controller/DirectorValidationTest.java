package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DirectorValidationTest {
    Director director;
    Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        director = new Director();
    }

    @Test
    void setDirectorWithCorrectData() {
        director.setId(1L);
        director.setName("Андрей Тарковский");

        Set<ConstraintViolation<Director>> violations = validator.validate(director);

        assertEquals(0, violations.size());
        assertTrue(violations.isEmpty(), "Ошибка валидации при заполнении класса Director");
    }

    @Test
    void setDirectorWithEmptyData() {
        Set<ConstraintViolation<Director>> violations = validator.validate(director);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), "Ошибка валидации при заполнении класса Director пустыми данными");
    }

    @Test
    void setDirectorWithSpaceInsteadOfName() {
        director.setId(1L);
        director.setName(" ");

        Set<ConstraintViolation<Director>> violations = validator.validate(director);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), "Ошибка валидации при заполнении класса Director пустыми данными");
    }
}