package ru.yandex.practicum.filmorate.storage.mappers.user;

import io.micrometer.common.util.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());

        String result = resultSet.getString("friends");
        if (!StringUtils.isBlank(result)) {
            Set<Long> friendsId = Stream.of(result.split(",")).map(Long::parseLong).collect(Collectors.toSet());
            user.setFriends(friendsId);
        } else {
            user.setFriends(new HashSet<>());
        }

        return user;
    }
}
