package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public boolean isUserWithEmailExist(String eMail) {
        return users.values().stream().anyMatch(userFromMemory -> userFromMemory.getEmail().equals(eMail));
    }

    public Optional<User> findUser(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public Collection<User> getUsers() {
        return users.values();
    }

    public User create(User user) {
        user.setId(getNextId());
        log.trace("Данные пользователя " + user.getName() + " сохранены!");
        users.put(user.getId(), user);
        return user;
    }

    public User update(User newUser) {
        log.trace("Данные пользователя " + newUser.getName() + " обновлены!");
        users.put(newUser.getId(), newUser);
        return newUser;
    }
}
