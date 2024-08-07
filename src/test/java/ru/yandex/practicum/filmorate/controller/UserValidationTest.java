package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {
    private User user;
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        user = new User();
    }

    @Test
    void setUsersWithCorrectData() {
        user.setId(1L);
        user.setEmail("User@yandex.ru");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 5, 12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(0, violations.size());
        assertTrue(violations.isEmpty(), "Ошибка валидации при заполнении класса User");
    }

    @Test
    void setUsersWithEmptyData() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(2, violations.size());
        assertFalse(violations.isEmpty(), "Ошибка валидации при заполнении класса User пустыми данными");
    }

    @Test
    void setUsersWithIncorrectEmail() {
        user.setId(1L);
        user.setEmail("Userddfyandexru");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 5, 12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());

        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setUsersWithSpaceOnEmail() {
        user.setId(1L);
        user.setEmail(" ");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 5, 12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(2, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setUsersWithEmptyEmail() {
        user.setId(1L);
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 5, 12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setUsersWithIncorrectLogin() {
        user.setId(1L);
        user.setEmail("User@yandex.ru");
        user.setLogin(" ");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 5, 12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(2, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setUsersWithSpaceOnLogin() {
        user.setId(1L);
        user.setEmail("User@yandex.ru");
        user.setLogin("Lo gin");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 5, 12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setUsersWithEmptyLogin() {
        user.setId(1L);
        user.setEmail("User@yandex.ru");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 5, 12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }

    @Test
    void setUsersWithEmptyNameFillLogin() {
        user.setId(1L);
        user.setEmail("User@yandex.ru");
        user.setLogin("Login");
        user.setBirthday(LocalDate.of(2000, 5, 12));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(0, violations.size());
        assertTrue(violations.isEmpty(), "Ошибка валидации при замене пустого имени логином");
    }

    @Test
    void setUsersWithIncorrectBirthDay() {
        user.setId(1L);
        user.setEmail("User@yandex.ru");
        user.setLogin("Login");
        user.setBirthday(LocalDate.of(2099, 12, 31));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertFalse(violations.isEmpty(), violations.stream().map(ConstraintViolation::getMessage).toList().get(0));
    }
}