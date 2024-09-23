package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MpaValidationTest {
    private Mpa mpa;
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        mpa = new Mpa();
    }

    @Test
    void setMpaWithCorrectData() {
        mpa.setId(1L);
        mpa.setName("G");
        mpa.setDescription("У фильма нет возрастных ограничений");

        Set<ConstraintViolation<Mpa>> violations = validator.validate(mpa);

        assertEquals(0, violations.size());
        assertTrue(violations.isEmpty(), "Ошибка валидации при заполнении класса Mpa");
    }

    @Test
    void setMpaWithEmptyData() {
        Set<ConstraintViolation<Mpa>> violations = validator.validate(mpa);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), "Ошибка валидации при заполнении класса Mpa пустыми данными");
    }

    @Test
    void setMpaWithEmptyName() {
        mpa.setId(1L);
        mpa.setDescription("У фильма нет возрастных ограничений");

        Set<ConstraintViolation<Mpa>> violations = validator.validate(mpa);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }
}
