package ru.yandex.practicum.filmorate.tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.UserRepositoryImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.Assert.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryImplTest {

    private final JdbcTemplate jdbcTemplate;

    private UserRepositoryImpl userRepositoryImpl;

    @BeforeEach
    public void setUp() {
        userRepositoryImpl = new UserRepositoryImpl(jdbcTemplate);
    }

//    @Test
//    public void testSave() {
//        User newUser = new User(
//                1,
//                "UserName",
//                "user@gmail.com",
//                "UserLogin",
//                LocalDate.of(1999, 12, 12));
//
//        assertDoesNotThrow(() -> userRepositoryImpl.save(newUser));
//    }

    @Test
    public void testSave() {
        User newUser = new User(
                1,
                "UserName",
                "user@gmail.com",
                "UserLogin",
                LocalDate.of(1999, 12, 12));

        assertDoesNotThrow(() -> {
            int rowsAffected = userRepositoryImpl.save(newUser).getId();
            System.out.println("Number of rows affected: " + rowsAffected);
            assertTrue(rowsAffected > 0); // Проверяем, что были затронуты какие-то строки
        });
    }


    //    @AfterEach
//    public void tearDown() {
//        jdbcTemplate.execute("DELETE FROM USERS");
//    }

//    @Test
//    @Commit
//    public void testSave() {
//        User newUser = new User(
//                1,
//                "UserName",
//                "user@gmail.com",
//                "UserLogin",
//                LocalDate.of(1999, 12, 12));
//
//        assertDoesNotThrow(() -> userRepositoryImpl.save(newUser));
//
//        User retrievedUser = jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE user_id = ?", new Object[]{newUser.getId()}, new RowMapper<User>() {
//            @Override
//            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//                return new User(
//                        rs.getInt("user_id"),
//                        rs.getString("name"),
//                        rs.getString("email"),
//                        rs.getString("login"),
//                        rs.getDate("date_of_birth").toLocalDate());
//            }
//        });
//
//        assertNotNull(retrievedUser);
//        assertEquals(newUser.getName(), retrievedUser.getName());
//        assertEquals(newUser.getEmail(), retrievedUser.getEmail());
//        assertEquals(newUser.getLogin(), retrievedUser.getLogin());
//        assertEquals(newUser.getBirthday(), retrievedUser.getBirthday());
//    }




    @Test
    public void testFindUserById() {
        // Подготавливаем данные для теста
        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov",
                LocalDate.of(1990, 1, 1));
        userRepositoryImpl.save(newUser);

        // вызываем тестируемый метод
        User savedUser = userRepositoryImpl.findById(newUser.getId());

        // System.out.println(savedUser);

        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают
    }

//    @Test
//    public void testFindUserByIdNoCreate() {
//
//        User savedUser = userRepositoryImpl.findById(1);
//
//        System.out.println(savedUser);
//
//    }



}
