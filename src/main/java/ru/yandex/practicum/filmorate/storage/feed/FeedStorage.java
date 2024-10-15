package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.model.Feed;

import java.util.Collection;

public interface FeedStorage {
    void addEvent(Feed feed);

    Collection<Feed> getEventsByUserId(Long userId);
}
