package ru.yandex.practicum.filmorate.storage.feed;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Feed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component("InMemoryFeedStorage")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InMemoryFeedStorage implements FeedStorage {

    private final List<Feed> feeds = new ArrayList<>();

    private long getNextId() {
        long currentMaxId = feeds.stream()
                .mapToLong(Feed::getEventId)
                .max()
                .orElse(0);
        return currentMaxId + 1;
    }

    @Override
    public void addEvent(Feed feed) {
        feed.setEventId(getNextId());
        feeds.add(feed);
    }

    @Override
    public Collection<Feed> getEventsByUserId(Long userId) {
        return feeds.stream()
                .filter(feed -> feed.getUserId().equals(userId))
                .sorted((f1, f2) -> Long.compare(f1.getTimestamp(), f2.getTimestamp()))
                .collect(Collectors.toList());
    }
}
