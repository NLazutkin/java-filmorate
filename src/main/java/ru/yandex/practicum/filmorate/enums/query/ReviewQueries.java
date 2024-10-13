package ru.yandex.practicum.filmorate.enums.query;

public enum ReviewQueries {
    INSERT_REVIEW_QUERY("INSERT INTO reviews(user_id, film_id, content, isPositive, useful)VALUES (?, ?, ?, ?, ?)"),

    FIND_BY_ID_QUERY("SELECT * FROM reviews WHERE id = ?"),

    FIND_ALL_QUERY("SELECT * FROM reviews"),

    FIND_N_REVIEWS_QUERY("SELECT * FROM reviews LIMIT ?"),

    FIND_REVIEWS_BY_FILM_QUERY("SELECT * FROM reviews WHERE film_id = ? LIMIT ?"),

    UPDATE_QUERY("UPDATE reviews SET user_id = ?, film_id = ?, content = ?, isPositive = ?, useful = ? WHERE id = ?"),

    DELETE_QUERY("DELETE FROM reviews WHERE id = ?"),

    INCREASE_USEFUL_QUERY("UPDATE reviews SET useful = useful + 1 WHERE id = ?"),

    DECREASE_USEFUL_QUERY("UPDATE reviews SET useful = useful - 1 WHERE id = ?");

    private final String query;

    ReviewQueries(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return this.query;
    }
}
