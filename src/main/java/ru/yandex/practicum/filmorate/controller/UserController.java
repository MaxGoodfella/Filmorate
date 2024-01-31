package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final List<User> users = new ArrayList<>();

    private int generatedID = 0;


    private int generateID() {
        return ++generatedID;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public User create(@Valid @RequestBody User newUser) {
    public User create(@RequestBody User newUser) {

        validateUser(newUser);

        for (User existingUser : users) {
            if (existingUser.getEmail().equals(newUser.getEmail())) {
                log.warn("Регистрация пользователя не удалась. Пользователь с электронной почтой {} уже существует.", newUser.getEmail());
                throw new UserAlreadyExistsException("Пользователь с электронной почтой " +
                        newUser.getEmail() + " уже зарегистрирован.");
            }
        }

        newUser.setId(generateID());
        users.add(newUser);

        log.info("Пользователь успешно зарегистрирован. Email: {}", newUser.getEmail());
        return newUser;
    }


    @PutMapping
    public User put(@Valid @RequestBody User updatedUser) {
    // public User put(@RequestBody User updatedUser) {
        validateUser(updatedUser);

        int idToUpdate = updatedUser.getId();

        for (User user : users) {
            if (user.getId() == idToUpdate) {
                user.setEmail(updatedUser.getEmail());
                user.setName(updatedUser.getName());
                user.setBirthday(updatedUser.getBirthday());
                user.setLogin(updatedUser.getLogin());

                log.info("Информация о пользователе с id {} успешно обновлена.", idToUpdate);
                return user;
            }
        }

        log.warn("Пользователь c id {} для обновления не найден.", idToUpdate);
        throw new UserNotFoundException("Пользователь с id " + idToUpdate + " не найден.");
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> findAll() {
        return users;
    }



    public void validateUser(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Адрес электронной почты {} не может быть пустым и должен содержать символ @.", user.getEmail());
            throw new ValidationException("Адрес электронной почты не может быть пустым и должен содержать символ @.");
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы.");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя отсутствует, в качестве имени будет использован логин {}", user.getLogin());
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

}