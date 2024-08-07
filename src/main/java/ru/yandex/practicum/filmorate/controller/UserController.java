package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private boolean isUserWithEmailExist(String eMail) {
        return users.values().stream().anyMatch(userFromMemory -> userFromMemory.getEmail().equals(eMail));
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Создаем запись пользователя");

        if (isUserWithEmailExist(user.getEmail())) {
            log.error("Этот E-mail уже используется");
            throw new DuplicatedDataException("Этот E-mail уже используется");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Использован логин вместо имени");
            user.setName(user.getLogin());
        }

        user.setId(getNextId());

        // сохраняем нового пользователя в памяти приложения
        log.debug("Пользователь \n" + user + "\n сохранен!");
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.debug("Обновляем данные пользователя");
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            log.error("Id пользователя должен быть указан");
            throw new ValidationException("Id пользователя должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {
            log.debug("Пользователь найден");
            User oldUser = users.get(newUser.getId());

            if (!isUserWithEmailExist(newUser.getEmail())) {
                oldUser.setEmail(newUser.getEmail());
            }

            oldUser.setLogin(newUser.getLogin());

            if (!newUser.getName().isBlank()) {
                log.debug("Имя пользователя обновлено");
                oldUser.setName(newUser.getName());
            }

            oldUser.setBirthday(newUser.getBirthday());

            return oldUser;
        }

        log.error("Пользователь с id = " + newUser.getId() + " не найден");
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }
}
