package ru.yandex.practicum.filmorate.enums.query;

public enum FilmQueries {
    FIND_ALL_QUERY("SELECT * FROM films"),

    FIND_POPULAR_QUERY("SELECT f.*, COUNT(l.user_id) AS likes_count FROM films AS f " +
            "LEFT JOIN likes AS l ON f.id = l.film_id " +
            "GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.rating_id " +
            "ORDER BY likes_count DESC LIMIT ?"),

    FIND_POPULAR_BY_YEAR_QUERY("SELECT f.*, COUNT(l.user_id) AS likes_count FROM films AS f " +
            "LEFT JOIN likes AS l ON f.id = l.film_id " +
            "WHERE EXTRACT(YEAR FROM CAST(releaseDate AS date)) = ? " +
            "GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.rating_id " +
            "ORDER BY likes_count DESC LIMIT ?"),

    FIND_POPULAR_BY_GENRE_QUERY("SELECT f.*, COUNT(l.user_id) AS likes_count FROM films AS f " +
            "LEFT JOIN likes AS l ON f.id = l.film_id " +
            "LEFT JOIN films_genres fg ON f.id = fg.film_id " +
            "WHERE fg.genre_id = ? " +
            "GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.rating_id " +
            "ORDER BY likes_count DESC LIMIT ?"),

    FIND_POPULAR_BY_GENRE_AND_YEAR_QUERY("SELECT f.*, COUNT(l.user_id) AS likes_count FROM films AS f " +
            "LEFT JOIN likes AS l ON f.id = l.film_id " +
            "LEFT JOIN films_genres fg ON f.id = fg.film_id " +
            "WHERE fg.genre_id = ? AND EXTRACT(YEAR FROM CAST(releaseDate AS date)) = ? " +
            "GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.rating_id " +
            "ORDER BY likes_count DESC LIMIT ?"),

    FIND_DIRECTOR_FILMS_QUERY("SELECT f.* FROM films_directors AS fd " +
            "LEFT JOIN films AS f ON fd.film_id = f.id " +
            "WHERE fd.director_id = ? "),

    FIND_DIRECTOR_FILMS_ORDER_YEAR_QUERY("SELECT f.* FROM films_directors AS fd " +
            "LEFT JOIN films AS f ON fd.film_id = f.id " +
            "WHERE fd.director_id = ? " +
            "ORDER BY EXTRACT(YEAR FROM f.releaseDate)"),

    FIND_DIRECTOR_FILMS_ORDER_LIKES_QUERY("SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.rating_id " +
            "FROM films_directors AS fd " +
            "LEFT JOIN films AS f ON fd.film_id = f.id " +
            "LEFT JOIN likes AS l ON l.film_id = f.id " +
            "WHERE fd.director_id = ? " +
            "GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.rating_id " +
            "ORDER BY COUNT(l.user_id) DESC"),

    SEARCH_FILMS_QUERY("SELECT f.*, COUNT(l.user_id) AS popularity " +
            "FROM films f " +
            "LEFT JOIN likes l ON f.id = l.film_id " +
            "%s " +
            "GROUP BY f.id " +
            "ORDER BY popularity DESC"
    ),

    FIND_BY_ID_QUERY("SELECT * FROM films WHERE id = ?"),

    FIND_RATING_ID_QUERY("SELECT rating_id FROM films AS f WHERE id = ?"),

    FIND_GENRE_ID_QUERY("SELECT genre_id FROM films_genres WHERE film_id = ?"),

    FIND_DIRECTOR_ID_QUERY("SELECT director_id FROM films_directors WHERE film_id = ?"),

    FIND_LIKES_BY_ID_QUERY("SELECT user_id FROM likes WHERE film_id = ?"),

    FIND_USER_FILMS_QUERY("SELECT f.* FROM films f " +
            "INNER JOIN likes l ON f.id = l.film_id " +
            "INNER JOIN users u ON u.id = l.user_id " +
            "WHERE u.id = ?"),

    INSERT_FILM_QUERY("INSERT INTO films(name, description, releaseDate, duration, rating_id)VALUES (?, ?, ?, ?, ?)"),

    INSERT_LIKE_QUERY("INSERT INTO likes(film_id, user_id)VALUES (?, ?)"),

    INSERT_FILM_GENRE_QUERY("INSERT INTO films_genres(film_id, genre_id)VALUES (?, ?)"),

    INSERT_FILM_DIRECTOR_QUERY("INSERT INTO films_directors(film_id, director_id)VALUES (?, ?)"),

    UPDATE_QUERY("UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, rating_id = ? WHERE id = ?"),

    DELETE_QUERY("DELETE FROM films WHERE id = ?"),

    DELETE_FILM_GENRE_QUERY("DELETE FROM films_genres WHERE film_id = ?"),

    DELETE_FILM_DIRECTOR_QUERY("DELETE FROM films_directors WHERE film_id = ?"),

    DELETE_FILM_GENRE_BY_IDS_QUERY("DELETE FROM films_genres WHERE film_id = ? AND genre_id = ?"),

    DELETE_FILM_DIRECTOR_BY_IDS_QUERY("DELETE FROM films_directors WHERE film_id = ? AND director_id = ?"),

    DELETE_LIKE_QUERY("DELETE FROM likes WHERE film_id = ? AND user_id = ?"),

    FIND_LIKED_FILMS_BY_USER_ID_QUERY("SELECT film_id FROM likes WHERE user_id = ?"),

    FIND_MOST_SIMILAR_USER("SELECT user_id, COUNT(*) AS common_likes " +
            "FROM likes " +
            "WHERE film_id IN (SELECT film_id FROM likes WHERE user_id = ?) " +
            "AND user_id NOT IN (?) " +
            "GROUP BY user_id " +
            "ORDER BY common_likes DESC " +
            "LIMIT 1");

    private final String query;

    FilmQueries(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return this.query;
    }
}