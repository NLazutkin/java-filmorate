package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.feed.FeedDto;
import ru.yandex.practicum.filmorate.model.Feed;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedMapper {

    public static FeedDto mapToFeedDto(Feed feed) {
        FeedDto dto = new FeedDto();
        dto.setEventId(feed.getEventId());
        dto.setUserId(feed.getUserId());
        dto.setTimestamp(feed.getTimestamp());
        dto.setEventType(feed.getEventType());
        dto.setOperation(feed.getOperation());
        dto.setEntityId(feed.getEntityId());
        return dto;
    }

    public static Feed mapToFeed(FeedDto dto) {
        Feed feed = new Feed();
        feed.setEventId(dto.getEventId());
        feed.setUserId(dto.getUserId());
        feed.setTimestamp(dto.getTimestamp());
        feed.setEventType(dto.getEventType());
        feed.setOperation(dto.getOperation());
        feed.setEntityId(dto.getEntityId());
        return feed;
    }
}
