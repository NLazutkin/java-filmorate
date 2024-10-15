package ru.yandex.practicum.filmorate.enums.query;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum  GenreQueries {
    FIND_ALL_QUERY("SELECT * FROM genres"),

    FIND_BY_ID_QUERY("SELECT * FROM genres WHERE id = ?"),

    FIND_BY_NAME_QUERY("SELECT * FROM genres WHERE name = ?"),

    INSERT_GENRE_QUERY("INSERT INTO genres(name)VALUES (?)"),

    UPDATE_QUERY("UPDATE genres SET name = ? WHERE id = ?"),

    DELETE_QUERY("DELETE FROM genres WHERE id = ?");

    String query;

    GenreQueries(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return this.query;
    }
}
