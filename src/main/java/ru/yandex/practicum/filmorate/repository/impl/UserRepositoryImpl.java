package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;
import java.util.Map;


@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final UserMapper userMapper;


    @Override
    public User save(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("user_id");

        Map<String, Object> parameters = userMapper.toMap(user);
        Number newUserId = simpleJdbcInsert.executeAndReturnKey(parameters);

        user.setId(newUserId.intValue());

        return user;
    }


    @Override
    public boolean update(User user) {
        String sqlQuery = "UPDATE USERS SET NAME = ?, EMAIL = ?, LOGIN = ?, DATE_OF_BIRTH = ? " +
                "WHERE USER_ID = ?";

        int rowsAffected = jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());

        return rowsAffected > 0;
    }


    @Override
    public User findById(Integer id) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM USERS WHERE USER_ID = ?",
                userMapper,
                id);

        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @Override
    public User findByName(String userName) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM USERS WHERE NAME = ?",
                userMapper,
                userName);
    }

    @Override
    public User findByEmail(String email) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM USERS WHERE EMAIL = ?",
                userMapper,
                email);

        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @Override
    public User findByLogin(String login) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM USERS WHERE LOGIN = ?",
                userMapper,
                login);

        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @Override
    public Integer findIdByName(String name) {
        String sql = "SELECT USER_ID FROM USERS WHERE NAME = ?";
        List<Integer> userIds = jdbcTemplate.queryForList(sql, Integer.class, name);

        if (!userIds.isEmpty()) {
            return userIds.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<User> findAll() {
       return jdbcTemplate.query(
               "SELECT * FROM USERS ORDER BY USER_ID",
               userMapper);
    }


    @Override
    public boolean deleteById(Integer userID) {
        String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";

        return jdbcTemplate.update(sqlQuery, userID) > 0;
    }

    @Override
    public boolean deleteAll() {
        String sqlQuery = "DELETE FROM USERS";

        return jdbcTemplate.update(sqlQuery) > 0;
    }


    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String sqlQuery = "INSERT INTO USER_FRIENDSHIP(USER_ID, FRIEND_ID) VALUES (?, ?)";

        jdbcTemplate.update(sqlQuery,
                userId,
                friendId);
    }

    @Override
    public boolean removeFriend(Integer userId, Integer friendId) {
        String sqlQuery = "DELETE FROM USER_FRIENDSHIP " +
                "WHERE USER_ID = ? AND FRIEND_ID = ?";
        int rowsDeleted = jdbcTemplate.update(sqlQuery, userId, friendId);
        return rowsDeleted > 0;
    }


    @Override
    public List<User> findFriendsById(Integer userId) {
        String sqlQuery = "SELECT U.* " +
                "FROM USERS AS U " +
                "JOIN USER_FRIENDSHIP AS UF ON U.USER_ID = UF.FRIEND_ID " +
                "WHERE UF.USER_ID = ? " +
                "ORDER BY U.USER_ID";

        return jdbcTemplate.query(sqlQuery, userMapper, userId);
    }

    @Override
    public List<User> getCommonFriends(Integer user1ID, Integer user2ID) {
        String sqlQuery = "SELECT U.* " +
                "FROM USERS AS U " +
                "JOIN USER_FRIENDSHIP AS UF1 ON U.USER_ID = UF1.FRIEND_ID " +
                "JOIN USER_FRIENDSHIP AS UF2 ON U.USER_ID = UF2.FRIEND_ID " +
                "WHERE UF1.USER_ID = ? AND UF2.USER_ID = ? " +
                "ORDER BY U.USER_ID";

        return jdbcTemplate.query(sqlQuery, userMapper, user1ID, user2ID);
    }

}