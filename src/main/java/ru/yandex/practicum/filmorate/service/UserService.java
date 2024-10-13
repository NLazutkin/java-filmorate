package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.Pair;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.enums.query.EventType;
import ru.yandex.practicum.filmorate.enums.query.OperationType;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {
    UserStorage userStorage;
    FilmStorage filmStorage;
    FilmService filmService;
    FeedStorage feedStorage;

    @Autowired
    public UserService(@Qualifier(/*"InMemoryUserStorage"*/"UserDbStorage") UserStorage userStorage,
                       @Qualifier("FilmDbStorage") FilmStorage filmStorage, FilmService filmService,
                       @Qualifier("FeedDbStorage") FeedStorage feedStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.feedStorage = feedStorage;
    }

    public UserDto findUser(Long userId) {
        return UserMapper.mapToUserDto(userStorage.findUser(userId));
    }

    public void addFriend(Long userId, Long friendId) {
        Pair<String, String> names = userStorage.addFriend(userId, friendId);

        log.debug(String.format("Добавляем %s в список друзей %s", names.getSecond(), names.getFirst()));

        Feed feed = new Feed();
        feed.setUserId(userId);
        feed.setTimestamp(System.currentTimeMillis());
        feed.setEventType(EventType.FRIEND);
        feed.setOperation(OperationType.ADD);
        feed.setEntityId(friendId);

        feedStorage.addEvent(feed);
    }

    public void deleteFriend(Long userId, Long friendId) {
        Pair<String, String> names = userStorage.deleteFriend(userId, friendId);

        log.debug(String.format("Удаляем %s из списка друзей %s", names.getSecond(), names.getFirst()));

        Feed feed = new Feed();
        feed.setUserId(userId);
        feed.setTimestamp(System.currentTimeMillis());
        feed.setEventType(EventType.FRIEND);
        feed.setOperation(OperationType.REMOVE);
        feed.setEntityId(friendId);

        feedStorage.addEvent(feed);
    }

    public Collection<UserDto> findFriends(Long userId) {
        Collection<User> friendsList = userStorage.findFriends(userId);

        if (friendsList.isEmpty()) {
            log.trace("Список друзей не содержит записей");
            return new ArrayList<>();
        }

        log.trace("Список друзей составлен");
        return friendsList.stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    public Collection<UserDto> findOther(Long userId, Long otherId) {
        log.debug("Получаем список общих друзей пользователей");
        Collection<UserDto> userFriends = findFriends(userId);
        Collection<UserDto> otherUserFriends = findFriends(otherId);
        userFriends.retainAll(otherUserFriends);
        return userFriends;
    }

    public Collection<UserDto> getUsers() {
        log.debug("Получаем записи всех пользователей");
        return userStorage.getUsers().stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    public UserDto create(NewUserRequest request) {
        log.debug("Создаем запись пользователя");

        if (userStorage.isUserWithEmailExist(request.getEmail())) {
            throw new DuplicatedDataException(String.format("Этот E-mail \"%s\" уже используется", request.getEmail()));
        }

        if (request.getName() == null || request.getName().isBlank()) {
            log.trace("Использован логин вместо имени");
            request.setName(request.getLogin());
        }

        User user = UserMapper.mapToUser(request);
        user = userStorage.create(user);

        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UpdateUserRequest request) {
        log.debug("Обновляем данные пользователя");

        if (request.getId() == null) {
            throw new ValidationException("Id пользователя должен быть указан");
        }

        User updatedUser = UserMapper.updateUserFields(userStorage.findUser(request.getId()), request);
        updatedUser = userStorage.update(updatedUser);

        return UserMapper.mapToUserDto(updatedUser);
    }

    public boolean delete(Long filmId) {
        User user = userStorage.findUser(filmId);
        log.debug(String.format("Удаляем данные пользователя %s", user.getName()));
        return userStorage.delete(filmId);
    }

    public Collection<FilmDto> getRecommendedFilms(Long userId) {
        User user = userStorage.findUser(userId);
        log.debug("Запрашиваем рекомендации фильмов для пользователя {}", userId);
        Collection<Film> recommendedFilms = filmStorage.getRecommendedFilms(userId);

        if (recommendedFilms.isEmpty()) {
            log.debug("Для пользователя {} не найдено рекомендаций", userId);
            return Collections.EMPTY_LIST;
        } else {
            log.debug("Для пользователя {} составлен список из {} фильмов(-a)", userId, recommendedFilms.size());
            return recommendedFilms.stream()
                    .map(filmService::fillFilmData)
                    .collect(Collectors.toList());
        }
    }
}
