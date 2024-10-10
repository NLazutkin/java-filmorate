package ru.yandex.practicum.filmorate.enums.query;

public enum FilmQueries {
    FIND_ALL_QUERY("SELECT * FROM films"),

    FIND_POPULAR_QUERY("SELECT f.* FROM films AS f " +
                        "LEFT JOIN (SELECT film_id, count(l.user_id) likes " +
    			                    "FROM likes AS l " +
    			                    "GROUP BY l.film_id " +
                                    "ORDER BY count(l.user_id) desc " +
    			                    "LIMIT 10) AS liked_films ON f.id = liked_films.film_id " +
                        "ORDER BY liked_films.likes desc"),

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

    SEARCH_FILMS_QUERY(
            "SELECT f.*, COUNT(l.user_id) AS popularity " +
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