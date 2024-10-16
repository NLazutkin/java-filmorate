package ru.yandex.practicum.filmorate.storage.feed;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.query.FeedQueries;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.feed.FeedRowMapper;

import java.util.Collection;

@Slf4j
@Component("FeedDbStorage")
@Primary
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public class FeedDbStorage extends BaseDbStorage<Feed> implements FeedStorage {
    private final FeedRowMapper feedRowMapper;

    @Autowired
    public FeedDbStorage(JdbcTemplate jdbc, FeedRowMapper feedRowMapper) {
        super(jdbc);
        this.feedRowMapper = feedRowMapper;
    }

    @Override
    public void addEvent(Feed feed) {
        try {
            insert(FeedQueries.INSERT_EVENT.toString(),
                    feed.getUserId(),
                    feed.getTimestamp(),
                    feed.getEventType().name(),
                    feed.getOperation().name(),
                    feed.getEntityId());
            log.debug("Событие добавлено: {}", feed);
        } catch (Exception e) {
            log.error("Ошибка при добавлении события: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Collection<Feed> getEventsByUserId(Long userId) {
        return findMany(FeedQueries.SELECT_EVENTS_BY_USER_ID.toString(), feedRowMapper, userId);
    }
}
