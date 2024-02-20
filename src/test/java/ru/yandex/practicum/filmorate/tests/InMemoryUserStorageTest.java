package ru.yandex.practicum.filmorate.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryUserStorageTest {
    private static InMemoryUserStorage inMemoryUserStorage;

    private Validator validator;


    @BeforeEach
    public void setUp() {
        inMemoryUserStorage = new InMemoryUserStorage();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    public void testCreateUserWithValidParameters() {
        User user = new User("e@mail.ru", "login", LocalDate.now());
        user.setName("name");

        assertDoesNotThrow(() -> inMemoryUserStorage.create(user));
    }


    @Test
    public void testCreateUserWithSameEmail_shouldThrowEntityAlreadyExistsException() {
        User user1 = new User("e@mail.ru", "login", LocalDate.now());
        User user2 = new User("e@mail.ru", "login", LocalDate.now());
        user1.setName("name");
        user2.setName("name");

        inMemoryUserStorage.create(user1);

        assertThrows(EntityAlreadyExistsException.class, () -> inMemoryUserStorage.create(user2));
    }


    @Test
    public void testCreateUserWithEmptyEmail_shouldThrowValidationException() {
        User user = new User("", "login", LocalDate.now());
        user.setName("name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Отсутствует электронная почта");
    }

    @Test
    public void testCreateUserWithoutAtSymbolInEmail_shouldThrowValidationException() {
        User user = new User("email.ru", "login", LocalDate.now());
        user.setName("name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Отсутствует символ @ в электронной почте");
    }

    @Test
    public void testCreateUserWithInvalidEmail_shouldThrowValidationException() {
        User user = new User("это-неправильный?эмейл@", "login", LocalDate.now());
        user.setName("name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Формат не соответствует формату электронной почты");
    }

    @Test
    public void testCreateUserWithSameLogin_shouldThrowEntityAlreadyExistsException() {
        User user1 = new User("e@mail.ru", "login", LocalDate.now());
        User user2 = new User("email@mail.ru", "login", LocalDate.of(1999, 12, 4));
        user1.setName("name1");
        user2.setName("name2");

        inMemoryUserStorage.create(user1);

        assertThrows(EntityAlreadyExistsException.class, () -> inMemoryUserStorage.create(user2));
    }

    @Test
    public void testCreateUserWithEmptyLogin_shouldThrowValidationException() {
        User user = new User("e@mail.ru", "", LocalDate.now());
        user.setName("name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Отсутствует логин");
    }

    @Test
    public void testCreateUserWithSpacesInLogin_shouldThrowValidationException() {
        User user = new User("e@mail.ru", "lo gin", LocalDate.now());
        user.setName("name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "В логине присутствуют пробелы");
    }

    @Test
    public void testCreateUserWithValidLogin_shouldNotThrowValidationException() {
        User user = new User("e@mail.ru", "login", LocalDate.now());
        user.setName("name");

        assertDoesNotThrow(() -> inMemoryUserStorage.create(user));
    }

    @Test
    public void testCreateUserWithEmptyName() {
        User user = new User("e@mail.ru", "login", LocalDate.now());
        user.setName("");

        inMemoryUserStorage.create(user);
        assertEquals("login", user.getName(), "Имя отсутствует, логин выступает в качестве имени");
    }

    @Test
    public void testCreateUserWithNoName() {
        User user = new User("e@mail.ru", "login", LocalDate.now());

        inMemoryUserStorage.create(user);
        assertEquals("login", user.getName(), "Имя отсутствует, логин выступает в качестве имени");
    }

    @Test
    public void testCreateUserInvalidBirthday_shouldThrowValidationException() {
        User user = new User("e@mail.ru", "login", LocalDate.of(2222, 12, 22));
        user.setName("name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Некорректная дата рождения");
    }


    @Test
    public void testPutUpdateExistingUser() {
        User existingUser = new User("e@mail.ru", "login", LocalDate.of(1991, 1, 1));
        existingUser.setName("User");
        inMemoryUserStorage.create(existingUser);

        User updatedUser = new User("e@mail.ru", "new_login", LocalDate.of(1995, 5, 5));
        updatedUser.setName("New User");
        updatedUser.setId(existingUser.getId());
        User resultUser = inMemoryUserStorage.put(updatedUser);


        assertEquals(updatedUser, resultUser, "Информация о пользователе должна быть обновлена");
        assertEquals("new_login", resultUser.getLogin(), "Логин не совпадает");
        assertEquals("New User", resultUser.getName(), "Имя не совпадает");
        assertEquals(LocalDate.of(1995, 5, 5), resultUser.getBirthday(), "Дата рождения не совпадает");
    }

    @Test
    public void testPutUserWithInvalidEmailWithoutAtSymbol_shouldThrowValidationException() {
        User user = new User("email.ru", "login", LocalDate.now());
        user.setName("name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Отсутствует символ @ в электронной почте");
    }

    @Test
    public void testPutUserWithInvalidEmailEmpty_shouldThrowValidationException() {
        User user = new User("", "login", LocalDate.now());
        user.setName("name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Некорректная электронная почта");
    }

    @Test
    public void testPutUserWithInvalidLoginWithSpaces_shouldThrowValidationException() {
        User user = new User("e@mail.ru", "lo gin", LocalDate.now());
        user.setName("name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "В логине присутствуют пробелы");
    }

    @Test
    public void testPutUserWithInvalidLoginEmpty_shouldThrowValidationException() {
        User user = new User("e@mail.ru", "", LocalDate.now());
        user.setName("name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Отсутствует логин");
    }

    @Test
    public void testPutUserWithInvalidBirthday_shouldThrowValidationException() {
        User user = new User("e@mail.ru", "login", LocalDate.of(2222, 12, 22));
        user.setName("name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Некорректная дата рождения");
    }

    @Test
    public void testPutUserWithNoName() {
        User existingUser = new User("e@mail.ru", "login", LocalDate.of(1991, 1, 1));
        inMemoryUserStorage.create(existingUser);

        User updatedUser = new User("e@mail.ru", "new_login", LocalDate.of(1995, 5, 5));
        updatedUser.setId(existingUser.getId());
        User resultUser = inMemoryUserStorage.put(updatedUser);

        assertEquals("new_login", resultUser.getName(), "Имя отсутствует, логин выступает в качестве имени");
    }


    @Test
    public void testFindAll() {
        List<User> users = inMemoryUserStorage.findAll();

        User user1 = new User("e@mail.ru", "login1", LocalDate.now());
        User user2 = new User("e@mail.com", "login2", LocalDate.now());
        user1.setName("User1");
        user2.setName("User2");
        inMemoryUserStorage.create(user1);
        inMemoryUserStorage.create(user2);

        assertEquals(user1, users.get(0), "Первый элемент не совпадает");
        assertEquals(user2, users.get(1), "Второй элемент не совпадает");
        assertEquals(2,users.size(), "Количество элементов не совпадает");


        User updatedUser1 = new User("e@mail.ru", "updated_login1", LocalDate.of(2010, 10, 10));
        User updatedUser2 = new User("e@mail.com", "updated_login2", LocalDate.of(2009, 9, 9));
        updatedUser1.setName("UpdatedUser1");
        updatedUser2.setName("UpdatedUser2");
        updatedUser1.setId(user1.getId());
        updatedUser2.setId(user2.getId());
        User resultUser1 = inMemoryUserStorage.put(updatedUser1);
        User resultUser2 = inMemoryUserStorage.put(updatedUser2);

        assertEquals(resultUser1, users.get(0), "Первый элемент не совпадает после обновления");
        assertEquals(resultUser2, users.get(1), "Второй элемент не совпадает после обновления");
        assertEquals(2,users.size(), "Количество элементов не совпадает после обновления");
    }


    @Test
    public void testAddFriend() {
        User user1 = new User("e1@mail.ru", "login1", LocalDate.now());
        User user2 = new User("e2@mail.ru", "login2", LocalDate.now());
        user1.setName("name1");
        user2.setName("name2");

        User createdUser1 = inMemoryUserStorage.create(user1);
        User createdUser2 = inMemoryUserStorage.create(user2);

        inMemoryUserStorage.addFriend(createdUser1.getId(), createdUser2.getId());

        List<User> user1Friends = inMemoryUserStorage.getAllFriends(createdUser1.getId());
        List<User> user2Friends = inMemoryUserStorage.getAllFriends(createdUser2.getId());


        assertTrue(user1Friends.contains(createdUser2),
                "Пользователь 2 находится в списке друзей пользователя 1");
        assertTrue(user2Friends.contains(createdUser1),
                "Пользователь 1 находится в списке друзей пользователя 2");
    }


    @Test
    public void testRemoveFriend() {
        User user1 = new User("e1@mail.ru", "login1", LocalDate.now());
        User user2 = new User("e2@mail.ru", "login2", LocalDate.now());
        user1.setName("name1");
        user2.setName("name2");

        User createdUser1 = inMemoryUserStorage.create(user1);
        User createdUser2 = inMemoryUserStorage.create(user2);

        inMemoryUserStorage.addFriend(createdUser1.getId(), createdUser2.getId());

        inMemoryUserStorage.removeFriend(createdUser1.getId(), createdUser2.getId());

        List<User> user1Friends = inMemoryUserStorage.getAllFriends(createdUser1.getId());
        List<User> user2Friends = inMemoryUserStorage.getAllFriends(createdUser2.getId());


        assertFalse(user1Friends.contains(createdUser2),
                "Пользователь 2 не находится в списке друзей пользователя 1");
        assertFalse(user2Friends.contains(createdUser2),
                "Пользователь 1 не находится в списке друзей пользователя 2");
    }



    @Test
    public void testRemoveFriend_shouldThrowEntityNotFoundException() {
        User user1 = new User("e1@mail.ru", "login1", LocalDate.now());
        User user2 = new User("e2@mail.ru", "login2", LocalDate.now());
        User user3 = new User("e3@mail.ru", "login3", LocalDate.now());
        user1.setName("name1");
        user2.setName("name2");
        user2.setName("name3");

        User createdUser1 = inMemoryUserStorage.create(user1);
        User createdUser2 = inMemoryUserStorage.create(user2);
        User createdUser3 = inMemoryUserStorage.create(user3);

        inMemoryUserStorage.addFriend(createdUser1.getId(), createdUser2.getId());


        assertThrows(EntityNotFoundException.class, () ->
                        inMemoryUserStorage.removeFriend(createdUser1.getId(), createdUser3.getId()),
                "Пользователь 3 не найден в списке друзей пользователя 1");
    }


    @Test
    public void testGetAllFriends() {
        User user1 = new User("e1@mail.ru", "login1", LocalDate.now());
        User user2 = new User("e2@mail.ru", "login2", LocalDate.now());
        User user3 = new User("e3@mail.ru", "login3", LocalDate.now());
        User user4 = new User("e4@mail.ru", "login4", LocalDate.now());
        user1.setName("name1");
        user2.setName("name2");
        user2.setName("name3");
        user2.setName("name4");

        User createdUser1 = inMemoryUserStorage.create(user1);
        User createdUser2 = inMemoryUserStorage.create(user2);
        User createdUser3 = inMemoryUserStorage.create(user3);
        User createdUser4 = inMemoryUserStorage.create(user4);

        inMemoryUserStorage.addFriend(createdUser1.getId(), createdUser2.getId());
        inMemoryUserStorage.addFriend(createdUser1.getId(), createdUser3.getId());
        inMemoryUserStorage.addFriend(createdUser1.getId(), createdUser4.getId());

        inMemoryUserStorage.removeFriend(createdUser1.getId(), createdUser2.getId());

        List<User> user1Friends = inMemoryUserStorage.getAllFriends(createdUser1.getId());
        List<User> user3Friends = inMemoryUserStorage.getAllFriends(createdUser3.getId());


        assertEquals(2, user1Friends.size(),
                "Количество друзей пользователя 1 не совпадает");
        assertFalse(user1Friends.contains(createdUser2),
                "Пользователь 2 не находится в списке друзей пользователя 1");
        assertTrue(user1Friends.contains(createdUser3),
                "Пользователь 3 находится в списке друзей пользователя 1");
        assertTrue(user1Friends.contains(createdUser4),
                "Пользователь 4 находится в списке друзей пользователя 1");

        assertEquals(1, user3Friends.size(),
                "Количество друзей пользователя 3 не совпадает");
        assertTrue(user3Friends.contains(createdUser1),
                "Пользователь 1 находится в списке друзей пользователя 3");
    }


    @Test
    public void testGetAllFriends_shouldThrowEntityNotFoundException() {
        User user1 = new User("e1@mail.ru", "login1", LocalDate.now());
        User user2 = new User("e2@mail.ru", "login2", LocalDate.now());
        user1.setName("name1");
        user2.setName("name2");

        inMemoryUserStorage.create(user1);
        inMemoryUserStorage.create(user2);


        assertThrows(EntityNotFoundException.class, () -> inMemoryUserStorage.getAllFriends(3),
                "Пользователь не найден");
    }


    @Test
    public void testGetMutualFriends() {
        User user1 = new User("e1@mail.ru", "login1", LocalDate.now());
        User user2 = new User("e2@mail.ru", "login2", LocalDate.now());
        User user3 = new User("e3@mail.ru", "login3", LocalDate.now());
        User user4 = new User("e4@mail.ru", "login4", LocalDate.now());
        user1.setName("name1");
        user2.setName("name2");
        user2.setName("name3");
        user2.setName("name4");

        User createdUser1 = inMemoryUserStorage.create(user1);
        User createdUser2 = inMemoryUserStorage.create(user2);
        User createdUser3 = inMemoryUserStorage.create(user3);
        User createdUser4 = inMemoryUserStorage.create(user4);

        inMemoryUserStorage.addFriend(createdUser1.getId(), createdUser3.getId());
        inMemoryUserStorage.addFriend(createdUser1.getId(), createdUser4.getId());
        inMemoryUserStorage.addFriend(createdUser2.getId(), createdUser3.getId());
        inMemoryUserStorage.addFriend(createdUser2.getId(), createdUser4.getId());

        inMemoryUserStorage.removeFriend(createdUser2.getId(), createdUser4.getId());

        List<User> mutualUser1User2 = inMemoryUserStorage.getMutualFriends(createdUser1.getId(), createdUser2.getId());


        assertFalse(mutualUser1User2.contains(createdUser4),
                "Пользователь 2 не находится в списке друзей пользователя 1");
        assertTrue(mutualUser1User2.contains(createdUser3),
                "Пользователь 3 находится в списке друзей пользователя 1");

        assertEquals(1, mutualUser1User2.size(), "Неверное количество общих друзей");
    }

    @Test
    public void testFindUserByID() {
        User user1 = new User("e1@mail.ru", "login1", LocalDate.now());
        User user2 = new User("e2@mail.ru", "login2", LocalDate.now());
        user1.setName("name1");
        user2.setName("name2");

        User createdUser1 = inMemoryUserStorage.create(user1);
        User createdUser2 = inMemoryUserStorage.create(user2);

        assertEquals(createdUser1, inMemoryUserStorage.findUserByID(createdUser1.getId()),
                "Пользователь 1 найден");
        assertEquals(createdUser2, inMemoryUserStorage.findUserByID(createdUser2.getId()),
                "Пользователь 2 найден");
    }

    @Test
    public void testFindUserByID_shouldThrowEntityNotFoundException() {
        User user1 = new User("e1@mail.ru", "login1", LocalDate.now());
        User user2 = new User("e2@mail.ru", "login2", LocalDate.now());
        user1.setName("name1");
        user2.setName("name2");

        assertThrows(EntityNotFoundException.class, () -> inMemoryUserStorage.findUserByID(user1.getId()),
                "Пользователь 1 не найден");
        assertThrows(EntityNotFoundException.class, () -> inMemoryUserStorage.findUserByID(user2.getId()),
                "Пользователь 2 не найден");
    }

}