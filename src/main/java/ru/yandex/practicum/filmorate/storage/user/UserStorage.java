package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.dto.Pair;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User findUser(Long userId);

    Collection<User> findFriends(Long userId);

    Collection<User> getUsers();

    User create(User user);

    User update(User newUser);

    boolean delete(Long id);

    Pair<String, String> addFriend(Long userId, Long friendId);

    Pair<String, String> deleteFriend(Long userId, Long friendId);

    boolean isUserWithEmailExist(String eMail);
}
