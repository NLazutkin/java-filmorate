package ru.yandex.practicum.filmorate.enums.query;

public enum FilmQueries {
    FIND_ALL_QUERY("SELECT * FROM films"),

    FIND_POPULAR_QUERY("SELECT f.* FROM films AS f " +
                        "INNER JOIN (SELECT film_id, count(l.user_id) likes " +
    			                    "FROM likes AS l " +
    			                    "GROUP BY l.film_id " +
                                    "ORDER BY count(l.user_id) desc " +
    			                    "LIMIT 10) AS liked_films ON f.id = liked_films.film_id " +
                        "ORDER BY liked_films.likes desc"),

    FIND_BY_ID_QUERY("SELECT * FROM films WHERE id = ?"),

    FIND_RATING_ID_QUERY("SELECT rating_id FROM films AS f WHERE id = ?"),

    FIND_GENRE_ID_QUERY("SELECT genre_id FROM films_genres WHERE film_id = ?"),

    FIND_LIKES_BY_ID_QUERY("SELECT user_id FROM likes WHERE film_id = ?"),

    FIND_USER_FILMS_QUERY("SELECT f.* FROM films f " +
                                "INNER JOIN likes l ON f.id = l.film_id " +
                                "INNER JOIN users u ON u.id = l.user_id " +
                                "WHERE u.id = ?"),

    INSERT_FILM_QUERY("INSERT INTO films(name, description, releaseDate, duration, rating_id)VALUES (?, ?, ?, ?, ?)"),

    INSERT_LIKE_QUERY("INSERT INTO likes(film_id, user_id)VALUES (?, ?)"),

    INSERT_FILM_GENRE_QUERY("INSERT INTO films_genres(film_id, genre_id)VALUES (?, ?)"),

    UPDATE_QUERY("UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ? WHERE id = ?"),

    DELETE_QUERY("DELETE FROM films WHERE id = ?"),

    DELETE_LIKE_QUERY("DELETE FROM likes WHERE film_id = ? AND user_id = ?");

    private final String query;

    FilmQueries(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return this.query;
    }
}

