package ru.yandex.practicum.filmorate.storage.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.Pair;
import ru.yandex.practicum.filmorate.enums.query.UserQueries;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.user.UserBaseRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.user.UserRowMapper;

import java.util.Collection;

@Slf4j
@Component("UserDbStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    RowMapper<User> baseMapper;
    RowMapper<User> fullDataMapper;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbc, UserBaseRowMapper baseMapper, UserRowMapper fullDataMapper) {
        super(jdbc);
        this.baseMapper = baseMapper;
        this.fullDataMapper = fullDataMapper;
    }

    private User findById(Long userId) {
        return findOne(UserQueries.FIND_BY_ID_QUERY.toString(), baseMapper, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c ID %d не найден", userId)));
    }

    @Override
    public boolean isUserWithEmailExist(String eMail) {
        return findOne(UserQueries.FIND_BY_EMAIL_QUERY.toString(), baseMapper, eMail).isPresent();
    }

    @Override
    public User findUser(Long userId) {
        return findOne(UserQueries.FIND_USER_QUERY.toString(), fullDataMapper, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c ID %d не найден", userId)));
    }

    @Override
    public Collection<User> getUsers() {
        return findMany(UserQueries.FIND_ALL_QUERY.toString(), fullDataMapper);
    }

    @Override
    public Collection<User> findFriends(Long userId) {
        User user = findById(userId);
        log.debug("Список друзей {}", user.getName());
        return findMany(UserQueries.FIND_ALL_FRIENDS_QUERY.toString(), fullDataMapper, userId);
    }

    @Override
    public Pair<String, String> addFriend(Long userId, Long friendId) {
        User user = findById(userId);
        User friend = findById(friendId);

        String errMsg = "Не удалось создать запись о добавлении в друзья";

        update(UserQueries.INSERT_FRIEND_QUERY.toString(), errMsg, userId, friendId, 1);

        return new Pair<>(user.getName(), friend.getName());
    }

    @Override
    public Pair<String, String> deleteFriend(Long userId, Long friendId) {
        User user = findById(userId);
        User friend = findById(friendId);

        delete(UserQueries.DELETE_FRIEND_QUERY.toString(), userId, friendId);

        return new Pair<>(user.getName(), friend.getName());
    }

    @Override
    public User create(User user) {
        long id = insert(UserQueries.INSERT_QUERY.toString(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public User update(User newUser) {
        update(UserQueries.UPDATE_QUERY.toString(), "Не удалось обновить данные пользователя", newUser.getEmail(),
                newUser.getLogin(), newUser.getName(), newUser.getBirthday(), newUser.getId());
        return newUser;
    }

    @Override
    public boolean delete(Long id) {
        return delete(UserQueries.DELETE_QUERY.toString(), id);
    }
}
