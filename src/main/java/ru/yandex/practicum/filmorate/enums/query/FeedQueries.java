package ru.yandex.practicum.filmorate.enums.query;

public enum FeedQueries {
    INSERT_EVENT("INSERT INTO events (user_id, timestamp, event_type, operation, entity_id) VALUES (?, ?, ?, ?, ?)"),
    SELECT_EVENTS_BY_USER_ID("SELECT * FROM events WHERE user_id = ? ORDER BY timestamp ASC");

    private final String query;

    FeedQueries(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return this.query;
    }
}