package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Optional<User> findUser(Long userId);

    Collection<User> getUsers();

    User create(User user);

    User update(User newUser);

    boolean isUserWithEmailExist(String eMail);
}
