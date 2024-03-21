package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


// не забыть про qualifier annotation
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public User findById(Integer id) {
        return jdbcTemplate.queryForObject(
                "select * from USERS where user_id = ?",
                userRowMapper(),
                id);
    } // doesn't work

    @Override
    public List<User> findAll() {
       return jdbcTemplate.query(
               "select * from USERS",
               userRowMapper());
    } // works


//    @Override
//    public User save(User user) {
//        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName("USERS")
//                .usingGeneratedKeyColumns("user_id");
//
//        int id = simpleJdbcInsert.executeAndReturnKey(userToMap(user)).intValue();
//        return user.setId(id);
//    }

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


//    @Override
//    public User save(User newUser) {
////        String sqlQuery = "insert into USERS(NAME, EMAIL, LOGIN, DATE_OF_BIRTH) " +
////                "values (?, ?, ?, ?)";
//
//        String sqlQuery = "insert into USERS(NAME, DATE_OF_BIRTH) " +
//                "values (?, ?)";
//
//        // log.info("Сохранение пользователя {}", newUser);
//
////        jdbcTemplate.update(sqlQuery,
////                newUser.getName(),
////                newUser.getEmail(),
////                newUser.getLogin(),
////                newUser.getBirthday());
//
//        jdbcTemplate.update(sqlQuery,
//                newUser.getName(),
//                newUser.getBirthday());
//
//        // log.info("Пользователь {} успешно сохранён", newUser);
//
//        log.error("Ошибка сохранения");
//
//        return newUser;
//    }
        //DOESN'T WORK!!!

        // работает, но надо подумать, как сделать так, чтобы он не пускал рейтинг с таким же именем - done with unique
        // есть проблема - когда создаю новый рейтинг с тем же именем, ему дается айдишник всё равно,
        // а так быть не должно!!! - надо фиксить

        // и ещё, кстати, он в теле ответа не показывает айди, т.е. ему дают айди, но пишет null почему-то
    // }


//    @Override
//    public User save(User user) {
//        String sql = "INSERT INTO USERS (NAME, EMAIL, LOGIN, DATE_OF_BIRTH) VALUES (?, ?, ?, ?)";
//
//        KeyHolder keyHolder = new GeneratedKeyHolder(); // Для получения сгенерированного идентификатора
//
//        try {
//            jdbcTemplate.update(connection -> {
//                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//                ps.setString(1, user.getName());
//                ps.setString(2, user.getEmail());
//                ps.setString(3, user.getLogin());
//                ps.setDate(4, Date.valueOf(user.getBirthday()));
//                return ps;
//            }, keyHolder);
//
//            if (keyHolder.getKey() != null) {
//                user.setId(keyHolder.getKey().intValue()); // Устанавливаем сгенерированный идентификатор в объект пользователя
//                log.info("Пользователь успешно сохранен с ID: {}", user.getId());
//                return user;
//            } else {
//                log.error("Не удалось получить сгенерированный ID пользователя");
//                return null;
//            }
//        } catch (DataAccessException e) {
//            log.error("Ошибка при сохранении пользователя", e);
//            return null;
//        }
//    }

//    @Override
//    public int save(User user) {
//        String sqlQuery = "INSERT INTO USERS (name, email, login, date_of_birth) VALUES (?, ?, ?, ?)";
//        int userId = -1; // Инициализируем userId значением -1, чтобы вернуть его в случае неудачи
//
//        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./db/filmorate", "sa", "password");
//             PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
//            stmt.setString(1, user.getName());
//            stmt.setString(2, user.getEmail());
//            stmt.setString(3, user.getLogin());
//            stmt.setDate(4, Date.valueOf(user.getBirthday()));
//
//            int affectedRows = stmt.executeUpdate();
//
//            if (affectedRows == 0) {
//                throw new SQLException("Вставка данных в таблицу не выполнена, ни одна строка не была изменена.");
//            }
//
//            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    userId = generatedKeys.getInt(1);
//                } else {
//                    throw new SQLException("Не удалось получить сгенерированный ключ пользователя.");
//                }
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace(); // Обработка ошибки. В реальном приложении лучше обработать исключение корректно.
//        }
//
//        return userId;
//    }







//    @Override
//    public void saveMany(List<User> newUsers) {
//        jdbcTemplate.batchUpdate(
//                "INSERT INTO USERS(NAME, EMAIL, LOGIN, DATE_OF_BIRTH) VALUES (?, ?, ?, ?)",
//                new BatchPreparedStatementSetter() {
//                    @Override
//                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        User user = newUsers.get(i);
//                        ps.setString(1, user.getName());
//                        ps.setString(2, user.getEmail());
//                        ps.setString(3, user.getLogin());
////                        // Преобразуем строку в формате "yyyy-MM-dd" в объект java.sql.Date
////                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
////                        try {
////                            java.util.Date date = sdf.parse(String.valueOf(user.getBirthday()));
////                            ps.setDate(4, new java.sql.Date(date.getTime()));
////                        } catch (ParseException e) {
////                            e.printStackTrace();
////                        }
//                        ps.setObject(4, user.getBirthday());
//
//                    }
//                    @Override
//                    public int getBatchSize() {
//                        return newUsers.size();
//                    }
//                }
//        );
//    }


    @Override
    public void saveMany(List<User> newUsers) {
        List<User> users = newUsers;
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


//    private Map<String, Object> userToMap(User user) {
//        return Map.of(
//                "name", user.getName(),
//                "email", user.getEmail(),
//                "login", user.getLogin(),
//                "date_of_birth", String.valueOf(user.getBirthday()));
//    }

    private Map<String, Object> userToMap(User user) {
        return Map.of(
                "name", user.getName(),
                "email", user.getEmail(),
                "login", user.getLogin(),
                "date_of_birth", user.getBirthday().toString());
    }

}
