package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.feed.FeedDto;
import ru.yandex.practicum.filmorate.mapper.FeedMapper;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FeedService {
    FeedStorage feedStorage;
    UserStorage userStorage;

    @Autowired
    public FeedService(@Qualifier("UserDbStorage"/*"InMemoryUserStorage"*/) UserStorage userStorage,
                       @Qualifier("FeedDbStorage"/*InMemoryFeedStorage*/) FeedStorage feedStorage) {
        this.userStorage = userStorage;
        this.feedStorage = feedStorage;
    }

    public List<FeedDto> getUserFeed(Long userId) {
        User user = userStorage.findUser(userId);

        log.debug("Получаем список действия пользователя {}", user.getName());

        Collection<Feed> feeds = feedStorage.getEventsByUserId(userId);
        return feeds.stream()
                .map(FeedMapper::mapToFeedDto)
                .collect(Collectors.toList());
    }
}
