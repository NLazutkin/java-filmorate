package ru.yandex.practicum.filmorate.enums.query;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum UserQueries {
    FIND_ALL_QUERY("SELECT u.id, u.email, u.login, u.name, u.birthday, GROUP_CONCAT(f.friend_id) AS friends " +
            "FROM users u " +
            "LEFT JOIN friends f ON u.id = f.user_id " +
            "LEFT JOIN statuses AS s ON f.status_id = s.id AND s.name = 'Подтверждённая' " +
            "GROUP BY u.id, u.email, u.login, u.name, u.birthday "),

    FIND_USER_QUERY("SELECT u.id, u.email, u.login, u.name, u.birthday, GROUP_CONCAT(f.friend_id) AS friends " +
            "FROM users u " +
            "LEFT JOIN friends f ON u.id = f.user_id " +
            "LEFT JOIN statuses AS s ON f.status_id = s.id AND s.name = 'Подтверждённая' " +
            "WHERE u.id = ? " +
            "GROUP BY u.id, u.email, u.login, u.name, u.birthday"),

    FIND_ALL_FRIENDS_QUERY("SELECT u1.id, u1.email, u1.login, u1.name, u1.birthday, GROUP_CONCAT(f1.friend_id) AS friends " +
            "FROM users u1 " +
            "LEFT JOIN friends f1 ON u1.id = f1.user_id " +
            "LEFT JOIN statuses AS s1 ON f1.status_id = s1.id AND s1.name = 'Подтверждённая' " +
            "WHERE u1.id IN (SELECT f2.friend_id " +
                            "FROM users u2 " +
                            "LEFT JOIN friends f2 ON u2.id = f2.user_id " +
                            "LEFT JOIN statuses AS s2 ON f2.status_id = s2.id AND s2.name = 'Подтверждённая' " +
                            "WHERE u2.id = ?) " +
                            "GROUP BY u1.id, u1.email, u1.login, u1.name, u1.birthday"),

    FIND_BY_EMAIL_QUERY("SELECT * FROM users WHERE email = ?"),

    FIND_BY_ID_QUERY("SELECT * FROM users WHERE id = ?"),

    INSERT_QUERY("INSERT INTO users(email, login, name, birthday)VALUES (?, ?, ?, ?)"),

    INSERT_FRIEND_QUERY("INSERT INTO friends(user_id, friend_id, status_id)VALUES (?, ?, ?)"),

    UPDATE_QUERY("UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?"),

    DELETE_QUERY("DELETE FROM users WHERE id = ?"),

    DELETE_FRIEND_QUERY("DELETE FROM friends WHERE user_id = ? AND friend_id = ?");

    String query;

    UserQueries(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return this.query;
    }
}
