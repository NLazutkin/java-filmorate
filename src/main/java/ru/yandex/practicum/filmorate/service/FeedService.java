package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.feed.FeedDto;
import ru.yandex.practicum.filmorate.mapper.FeedMapper;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FeedService {

    private final FeedStorage feedStorage;

    public List<FeedDto> getUserFeed(Long userId) {
        Collection<Feed> feeds = feedStorage.getEventsByUserId(userId);
        return feeds.stream()
                .map(FeedMapper::mapToFeedDto)
                .collect(Collectors.toList());
    }
}
