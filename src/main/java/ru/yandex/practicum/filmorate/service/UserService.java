package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User findUser(Long userId) {
        return userStorage.findUser(userId).orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
    }

    public void addFriend(Long userId, Long friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);
        log.debug("Добавляем " + friend.getName() + " в список друзей " + user.getName());

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);
        log.debug("Удаляем " + friend.getName() + " из списка друзей " + user.getName());

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public Collection<User> findFriends(Long userId) {
        User user = findUser(userId);
        log.debug("Список друзей " + user.getName());
        Set<Long> friendsIds = user.getFriends();

        if (friendsIds.isEmpty()) {
            log.trace("Список друзей " + user.getName() + " не содержит записей");
            return new ArrayList<>();
        }

        ArrayList<User> friendsList = new ArrayList<>();
        for (Long id : friendsIds) {
            friendsList.add(findUser(id));
        }

        log.trace("Список друзей составлен");
        return friendsList;
    }

    public Collection<User> findOther(Long userId, Long otherId) {
        log.debug("Получаем список общих друзей пользователей");
        Collection<User> userFriends = findFriends(userId);
        Collection<User> otherUserFriends = findFriends(otherId);
        userFriends.retainAll(otherUserFriends);
        return userFriends;
    }

    public Collection<User> getUsers() {
        log.debug("Получаем записи всех пользователей");
        return userStorage.getUsers();
    }

    public User create(User user) {
        log.debug("Создаем запись пользователя");

        if (userStorage.isUserWithEmailExist(user.getEmail())) {
            throw new DuplicatedDataException("Этот E-mail уже используется");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.trace("Использован логин вместо имени");
            user.setName(user.getLogin());
        }

        return userStorage.create(user);
    }

    public User update(User newUser) {
        log.debug("Обновляем данные пользователя");

        if (newUser.getId() == null) {
            throw new ValidationException("Id пользователя должен быть указан");
        }

        Optional<User> oldUser = userStorage.findUser(newUser.getId());
        if (oldUser.isPresent()) {
            log.trace("Пользователь найден");

            if (!userStorage.isUserWithEmailExist(newUser.getEmail())) {
                oldUser.get().setEmail(newUser.getEmail());
            }

            oldUser.get().setLogin(newUser.getLogin());

            if (!newUser.getName().isBlank()) {
                log.trace("Имя пользователя обновлено");
                oldUser.get().setName(newUser.getName());
            }

            oldUser.get().setBirthday(newUser.getBirthday());

            return userStorage.update(oldUser.get());
        }

        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }
}
