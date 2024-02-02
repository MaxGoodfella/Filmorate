package ru.yandex.practicum.filmorate.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class UserControllerTest {

    private static UserController userController;

    private Validator validator;


    @BeforeEach
    public void setUp() {
        userController = new UserController();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    public void testCreateUserWithValidParameters_shouldNotThrowValidationException() {
        User user = new User("e@mail.ru", "login", LocalDate.now());
        user.setName("name");

        assertDoesNotThrow(() -> userController.create(user));
    }


    @Test
    public void testCreateUserWithSameEmail_shouldThrowUserAlreadyExistsException() {
        User user1 = new User("e@mail.ru", "login", LocalDate.now());
        User user2 = new User("e@mail.ru", "login", LocalDate.now());
        user1.setName("name");
        user2.setName("name");

        userController.create(user1);

        assertThrows(EntityAlreadyExistsException.class, () -> userController.create(user2));
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

        assertDoesNotThrow(() -> userController.create(user));
    }

    @Test
    public void testCreateUserWithEmptyName() {
        User user = new User("e@mail.ru", "login", LocalDate.now());
        user.setName("");

        userController.create(user);
        assertEquals("login", user.getName(), "Имя отсутствует, логин выступает в качестве имени");
    }

    @Test
    public void testCreateUserWithNoName() {
        User user = new User("e@mail.ru", "login", LocalDate.now());

        userController.create(user);
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
        userController.create(existingUser);

        User updatedUser = new User("e@mail.ru", "new_login", LocalDate.of(1995, 5, 5));
        updatedUser.setName("New User");
        updatedUser.setId(existingUser.getId());
        User resultUser = userController.put(updatedUser);


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
        userController.create(existingUser);

        User updatedUser = new User("e@mail.ru", "new_login", LocalDate.of(1995, 5, 5));
        updatedUser.setId(existingUser.getId());
        User resultUser = userController.put(updatedUser);

        assertEquals("new_login", resultUser.getName(), "Имя отсутствует, логин выступает в качестве имени");
    }


    @Test
    public void testFindAll() {
        List<User> users = userController.findAll();

        User user1 = new User("e@mail.ru", "login1", LocalDate.now());
        User user2 = new User("e@mail.com", "login2", LocalDate.now());
        user1.setName("User1");
        user2.setName("User2");
        userController.create(user1);
        userController.create(user2);

        assertEquals(user1, users.get(0), "Первый элемент не совпадает");
        assertEquals(user2, users.get(1), "Второй элемент не совпадает");
        assertEquals(2,users.size(), "Количество элементов не совпадает");


        User updatedUser1 = new User("e@mail.ru", "updated_login1", LocalDate.of(2010, 10, 10));
        User updatedUser2 = new User("e@mail.com", "updated_login2", LocalDate.of(2009, 9, 9));
        updatedUser1.setName("UpdatedUser1");
        updatedUser2.setName("UpdatedUser2");
        updatedUser1.setId(user1.getId());
        updatedUser2.setId(user2.getId());
        User resultUser1 = userController.put(updatedUser1);
        User resultUser2 = userController.put(updatedUser2);

        assertEquals(resultUser1, users.get(0), "Первый элемент не совпадает после обновления");
        assertEquals(resultUser2, users.get(1), "Второй элемент не совпадает после обновления");
        assertEquals(2,users.size(), "Количество элементов не совпадает после обновления");
    }

}