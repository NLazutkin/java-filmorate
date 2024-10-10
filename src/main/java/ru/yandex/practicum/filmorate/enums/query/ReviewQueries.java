package ru.yandex.practicum.filmorate.enums.query;

public enum ReviewQueries {
    //???
    FIND_LIKES_BY_ID_QUERY("SELECT user_id FROM reviews_likes WHERE review_id = ?"),

    INSERT_LIKE_QUERY("INSERT INTO reviews_likes(review_id, user_id, isLike)VALUES (?, ?, ?)"),

    UPDATE_LIKE_QUERY("UPDATE reviews_likes SET isLike = ? WHERE review_id = ? AND user_id = ?"),

    DELETE_LIKE_QUERY("DELETE FROM reviews_likes WHERE review_id = ? AND user_id = ?"),

    INSERT_REVIEW_QUERY("INSERT INTO reviews(user_id, film_id, content, isPositive, useful)VALUES (?, ?, ?, ?, ?)"),

    FIND_BY_ID_QUERY("SELECT * FROM reviews WHERE id = ?"),

    FIND_ALL_QUERY("SELECT * FROM reviews"),

    //???
    FIND_N_REVIEWS_QUERY("SELECT * FROM reviews LIMIT ?"),

    //???
    FIND_REVIEWS_BY_FILM_QUERY("SELECT * FROM reviews WHERE film_id = ? LIMIT ?"),

    UPDATE_QUERY("UPDATE reviews SET user_id = ?, film_id = ?, content = ?, isPositive = ?, useful = ? WHERE id = ?"),

    DELETE_QUERY("DELETE FROM reviews WHERE id = ?");

    private final String query;

    ReviewQueries(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return this.query;
    }
}
