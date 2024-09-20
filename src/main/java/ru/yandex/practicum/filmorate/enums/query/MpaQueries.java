package ru.yandex.practicum.filmorate.enums.query;

public enum MpaQueries {
    FIND_ALL_QUERY("SELECT * FROM ratings"),

    FIND_BY_ID_QUERY("SELECT * FROM ratings WHERE id = ?"),

    FIND_BY_NAME_QUERY("SELECT * FROM ratings WHERE name = ?"),

    INSERT_MPA_QUERY("INSERT INTO ratings(name, description)VALUES (?, ?)"),

    UPDATE_QUERY("UPDATE ratings SET name = ?, description = ? WHERE id = ?"),

    DELETE_QUERY("DELETE FROM ratings WHERE id = ?");

    private final String query;

    MpaQueries(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return this.query;
    }
}
