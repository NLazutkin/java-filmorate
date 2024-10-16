package ru.yandex.practicum.filmorate.storage.mappers.feed;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.actions.EventType;
import ru.yandex.practicum.filmorate.enums.actions.OperationType;
import ru.yandex.practicum.filmorate.model.Feed;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FeedRowMapper implements RowMapper<Feed> {
    @Override
    public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
        Feed feed = new Feed();
        feed.setEventId(rs.getLong("event_id"));
        feed.setUserId(rs.getLong("user_id"));
        feed.setTimestamp(rs.getLong("timestamp"));
        feed.setEventType(EventType.valueOf(rs.getString("event_type")));
        feed.setOperation(OperationType.valueOf(rs.getString("operation")));
        feed.setEntityId(rs.getLong("entity_id"));
        return feed;
    }
}
