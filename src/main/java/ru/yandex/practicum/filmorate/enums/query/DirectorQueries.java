
package ru.yandex.practicum.filmorate.enums.query;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum DirectorQueries {
    FIND_ALL_QUERY("SELECT * FROM directors"),

    FIND_BY_ID_QUERY("SELECT * FROM directors WHERE id = ?"),

    FIND_BY_NAME_QUERY("SELECT * FROM directors WHERE name = ?"),

    INSERT_DIRECTOR_QUERY("INSERT INTO directors(name)VALUES (?)"),

    UPDATE_QUERY("UPDATE directors SET name = ? WHERE id = ?"),

    DELETE_QUERY("DELETE FROM directors WHERE id = ?");

    String query;

    DirectorQueries(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return this.query;
    }
}
