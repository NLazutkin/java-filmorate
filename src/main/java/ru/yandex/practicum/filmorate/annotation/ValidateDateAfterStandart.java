package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ValidateDateAfterStandart implements ConstraintValidator<DateAfterStandart, LocalDate> {
    LocalDate standartDate = LocalDate.MIN;

    @Override
    public void initialize(DateAfterStandart constraintAnnotation) {
        standartDate = LocalDate.parse(constraintAnnotation.standartDate());
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null) {
            return false;
        }

        return localDate.isAfter(standartDate);
    }
}
