package ru.yandex.practicum.filmorate.enums.query;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum EstimationQueries {
    INSERT_ESTIMATION_QUERY("INSERT INTO reviews_likes(review_id, user_id, isLike)VALUES (?, ?, ?)"),

    FIND_BY_IDS_QUERY("SELECT * FROM reviews_likes WHERE review_id = ? AND user_id = ?"),

    UPDATE_ESTIMATION_QUERY("UPDATE reviews_likes SET isLike = ? WHERE review_id = ? AND user_id = ?"),

    DELETE_ESTIMATION_QUERY("DELETE FROM reviews_likes WHERE review_id = ? AND user_id = ? AND isLike = ?");

    String query;

    EstimationQueries(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return this.query;
    }
}
