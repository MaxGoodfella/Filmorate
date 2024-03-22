package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public User save(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("user_id");

        Map<String, Object> parameters = userToMap(user);
        Number newUserId = simpleJdbcInsert.executeAndReturnKey(parameters);

        user.setId(newUserId.intValue());

        return user;
    }

    @Override
    public void saveMany(List<User> newUsers) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO USERS(NAME, EMAIL, LOGIN, DATE_OF_BIRTH) VALUES (?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        User user = newUsers.get(i);
                        ps.setString(1, user.getName());
                        ps.setString(2, user.getEmail());
                        ps.setString(3, user.getLogin());
                        ps.setDate(4, Date.valueOf(user.getBirthday()));
                    }

                    @Override
                    public int getBatchSize() {
                        return newUsers.size();
                    }
                });
    }


    @Override
    public User findById(Integer id) {
        return jdbcTemplate.queryForObject(
                "select * from USERS where user_id = ?",
                userRowMapper(),
                id);
    }

    @Override
    public List<User> findAll() {
       return jdbcTemplate.query(
               "select * from USERS",
               userRowMapper());
    }


    @Override
    public boolean deleteById(Integer userID) {
        String sqlQuery = "delete from USERS where user_id = ?";

        return jdbcTemplate.update(sqlQuery, userID) > 0;
    }

    @Override
    public boolean deleteAll() {
        String sqlQuery = "delete from USERS";

        return jdbcTemplate.update(sqlQuery) > 0;
    }


    private static RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(
                rs.getInt("user_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getDate("date_of_birth").toLocalDate());

    }

    private Map<String, Object> userToMap(User user) {
        return Map.of(
                "name", user.getName(),
                "email", user.getEmail(),
                "login", user.getLogin(),
                "date_of_birth", user.getBirthday().toString());
    }

}
