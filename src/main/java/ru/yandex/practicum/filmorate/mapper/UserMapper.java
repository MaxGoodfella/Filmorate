package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Component
public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getDate("date_of_birth").toLocalDate());
    }

    public Map<String, Object> toMap(User user) {
        return Map.of(
                "name", user.getName(),
                "email", user.getEmail(),
                "login", user.getLogin(),
                "date_of_birth", user.getBirthday().toString());
    }

}