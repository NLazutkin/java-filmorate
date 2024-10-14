package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.enums.actions.EventType;
import ru.yandex.practicum.filmorate.enums.actions.OperationType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.feed.FeedDbStorage;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FeedStorageTest {
    FeedDbStorage feedDbStorage;

    @Test
    public void testFindFilmById() {
        Feed feed1 = new Feed();
        feed1.setEventId(1L);
        feed1.setUserId(1L);
        feed1.setTimestamp(System.currentTimeMillis());
        feed1.setEventType(EventType.FRIEND);
        feed1.setOperation(OperationType.ADD);
        feed1.setEntityId(1L);

        feedDbStorage.addEvent(feed1);

        Feed feed2 = new Feed();
        feed2.setEventId(2L);
        feed2.setUserId(1L);
        feed2.setTimestamp(System.currentTimeMillis());
        feed2.setEventType(EventType.REVIEW);
        feed2.setOperation(OperationType.UPDATE);
        feed2.setEntityId(2L);

        feedDbStorage.addEvent(feed2);

        assertThat(feedDbStorage.getEventsByUserId(1L)).isNotEmpty()
                .hasSize(2)
                .filteredOn("eventType", EventType.REVIEW)
                .isNotEmpty()
                .hasExactlyElementsOfTypes(Feed.class);
    }

}
