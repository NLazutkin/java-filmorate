package ru.yandex.practicum.filmorate.dto.user;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    Long id;
    @NotBlank(message = "E-mail должен быть указан")
    @Email(message = "Email должен быть в формате user@yandex.ru")
    String email;
    @NotBlank(message = "Логин должен быть указан")
    @Pattern(regexp = "^\\S*$", message = "Логин не должен содержать символ \"пробел\"")
    String login;
    String name;
    @PastOrPresent(message = "Дата рождения не может быть больше текущего дня")
    LocalDate birthday;

    public boolean hasName() {
        return !StringUtils.isBlank(this.name);
    }

    public boolean hasBirthday() {
        return this.birthday != null;
    }
}
