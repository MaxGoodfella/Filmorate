package ru.yandex.practicum.filmorate.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserServiceImplementation;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceImplementationTest {

    private static UserServiceImplementation userServiceImplementation;


    @BeforeEach
    public void setUp() {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        userServiceImplementation = new UserServiceImplementation(inMemoryUserStorage);
    }


    @Test
    public void testCreateUserWithEmptyName() {
        User user = new User("e@mail.ru", "login", LocalDate.now());
        user.setName("");

        userServiceImplementation.create(user);
        assertEquals("login", user.getName(), "Имя отсутствует, логин выступает в качестве имени");
    }

    @Test
    public void testCreateUserWithNoName() {
        User user = new User("e@mail.ru", "login", LocalDate.now());

        userServiceImplementation.create(user);
        assertEquals("login", user.getName(), "Имя отсутствует, логин выступает в качестве имени");
    }

    @Test
    public void testPutUserWithNoName() {
        User existingUser = new User("e@mail.ru", "login", LocalDate.of(1991, 1, 1));
        userServiceImplementation.create(existingUser);

        User updatedUser = new User("e@mail.ru", "new_login", LocalDate.of(1995, 5, 5));
        updatedUser.setId(existingUser.getId());
        User resultUser = userServiceImplementation.put(updatedUser);

        assertEquals("new_login", resultUser.getName(), "Имя отсутствует, логин выступает в качестве имени");
    }

}