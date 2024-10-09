package ru.yandex.practicum.filmorate.enums.query;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public enum ReviewQueries {
    FIND_ALL_QUERY("SELECT * FROM reviews"),
    //???
    FIND_POPULAR_QUERY("SELECT f.* FROM films AS f " +
                               "INNER JOIN (SELECT film_id, count(l.user_id) likes " +
                               "FROM likes AS l " +
                               "GROUP BY l.film_id " +
                               "ORDER BY count(l.user_id) desc " +
                               "LIMIT 10) AS liked_films ON f.id = liked_films.film_id " +
                               "ORDER BY liked_films.likes desc"),
    //???
    FIND_LIKES_BY_ID_QUERY("SELECT user_id FROM reviews_likes WHERE review_id = ?"),

    INSERT_LIKE_QUERY("INSERT INTO reviews_likes(review_id, user_id, isLike)VALUES (?, ?, ?)"),

    DELETE_LIKE_QUERY("DELETE FROM reviews_likes WHERE review_id = ? AND user_id = ?"),

    UPDATE_LIKE_QUERY("UPDATE reviews_likes SET isLike = ? WHERE review_id = ? AND user_id = ?"),

    FIND_BY_ID_QUERY("SELECT * FROM reviews WHERE id = ?"),

    INSERT_REVIEW_QUERY("INSERT INTO reviews(user_id, film_id, content, isPositive, useful)VALUES (?, ?, ?, ?, ?)"),

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
