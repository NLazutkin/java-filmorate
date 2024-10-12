package ru.yandex.practicum.filmorate.storage.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.Pair;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("InMemoryUserStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InMemoryUserStorage implements UserStorage {
    Map<Long, User> users = new HashMap<>();

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

    public User findUser(Long userId) {
        return Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c ID %d не найден", userId)));
    }

    public Collection<User> getUsers() {
        return users.values();
    }

    public Collection<User> findFriends(Long userId) {
        User user = findUser(userId);
        log.debug("Список друзей пользователя {}", user.getName());
        Set<Long> friendsIds = user.getFriends();

        return friendsIds.stream().map(this::findUser).collect(Collectors.toList());
    }

    public Pair<String, String> addFriend(Long userId, Long friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);

        user.getFriends().add(friendId);

        return new Pair<>(user.getName(), friend.getName());
    }

    public Pair<String, String> deleteFriend(Long userId, Long friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);

        user.getFriends().remove(friendId);

        return new Pair<>(user.getName(), friend.getName());
    }

    public User create(User user) {
        user.setId(getNextId());
        log.trace("Данные пользователя {} сохранены!", user.getName());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User newUser) {
        log.trace("Данные пользователя {} обновлены!", newUser.getName());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public boolean delete(Long userId) {
        users.remove(userId);
        return Optional.ofNullable(users.get(userId)).isPresent();
    }
}
