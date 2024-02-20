package ru.yandex.practicum.filmorate.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryFilmStorageTest {
    private static InMemoryFilmStorage inMemoryFilmStorage;

    private static InMemoryUserStorage inMemoryUserStorage;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        inMemoryUserStorage = new InMemoryUserStorage();
        inMemoryFilmStorage = new InMemoryFilmStorage(inMemoryUserStorage);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testCreateFilmWithValidParameters() {
        Film film = new Film("FilmName", "Description", LocalDate.now(), 1000);

        assertDoesNotThrow(() -> inMemoryFilmStorage.create(film));
    }

    @Test
    public void testCreateSameFilm_shouldThrowEntityAlreadyExistsException() {
        Film film1 = new Film("FilmName", "Description", LocalDate.now(), 1000);
        Film film2 = new Film("FilmName", "Description", LocalDate.now(), 1000);

        inMemoryFilmStorage.create(film1);
        assertThrows(EntityAlreadyExistsException.class, () -> inMemoryFilmStorage.create(film2));
    }

    @Test
    public void testCreateFilmWithEmptyName_shouldThrowValidationException() {
        Film film = new Film("", "Description", LocalDate.now(), 1000);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Отсутствует название фильма");
    }

    @Test
    public void testCreateFilmWithNegativeDuration_shouldThrowValidationException() {
        Film film = new Film("FilmName", "Description", LocalDate.now(), -1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Некорректная продолжительность фильма");
    }

    @Test
    public void testCreateFilmWithZeroDuration_shouldThrowValidationException() {
        Film film = new Film("FilmName", "Description", LocalDate.now(), 0);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Некорректная продолжительность фильма");
    }

    @Test
    void testCreateFilmWithInvalidDescription_shouldThrowValidationException() {
        String invalidDescription = "The description that is much too long to be accepted by the service. " +
                "Next time try to come up with something shorter. " +
                "I have no idea what else to sssssssssssssssssssssssssaaaaaaaaaaaaaaaaaaaaaaaaaay here";

        Film film = new Film("FilmName", invalidDescription, LocalDate.now(), 1000);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Некорректное описание фильма");
    }

    @Test
    void testCreateFilmWithInvalidReleaseDate_shouldThrowValidationException() {
        LocalDate releaseDateBefore18951228 = LocalDate.of(1895, 12, 27);

        Film film = new Film("FilmName", "Description", releaseDateBefore18951228, 1000);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Некорректная дата выхода фильма");
    }

    @Test
    void testCreateFilmWithReleaseDate18951228_shouldNotThrowValidationException() {
        LocalDate releaseDate18951228 = LocalDate.of(1895, 12, 28);

        Film film = new Film("FilmName", "Description", releaseDate18951228, 1000);

        assertDoesNotThrow(() -> inMemoryFilmStorage.create(film));
    }


    @Test
    public void testPutUpdateExistingFilm() {
        Film initialFilm = new Film("FilmName", "Description", LocalDate.now(), 1000);
        inMemoryFilmStorage.create(initialFilm);

        Film updatedFilm = new Film("FilmName", "UpdatedDescription", LocalDate.of(1999,9,9), 2000);
        updatedFilm.setId(initialFilm.getId());
        Film resultFilm = inMemoryFilmStorage.put(updatedFilm);

        assertEquals(updatedFilm, resultFilm, "Фильм должен быть обновлен");
        assertEquals("UpdatedDescription", resultFilm.getDescription(), "Описание фильма должно быть обновлено");
    }

    @Test
    public void testPutFilmWithInvalidName_shouldThrowValidationException() {
        Film film = new Film("", "Description", LocalDate.now(), 1000);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Отсутствует названия фильма");
    }

    @Test
    public void testPutFilmWithInvalidDescription_shouldThrowValidationException() {
        String invalidDescription = "The description that is much too long to be accepted by the service. " +
                "Next time try to come up with something shorter. " +
                "I have no idea what else to sssssssssssssssssssssssssaaaaaaaaaaaaaaaaaaaaaaaaaay here";

        Film film = new Film("FilmName", invalidDescription, LocalDate.now(), 1000);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Некорректное описание фильма");
    }

    @Test
    public void testPutFilmWithInvalidReleaseDate_shouldThrowValidationException() {
        Film film = new Film("FilmName", "Description",
                LocalDate.of(1895,12,27), 1000);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Некорректная дата выхода фильма");
    }

    @Test
    public void testPutFilmWithInvalidDuration_shouldThrowValidationException() {
        Film film = new Film("FilmName", "Description", LocalDate.now(), 0);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Некорректная продолжительность фильма");
    }


    @Test
    public void testFindAll() {
        List<Film> films = inMemoryFilmStorage.findAll();

        Film film1 = new Film("FilmName1", "Description1", LocalDate.now(), 1000);
        Film film2 = new Film("FilmName2", "Description2", LocalDate.now(), 2000);
        inMemoryFilmStorage.create(film1);
        inMemoryFilmStorage.create(film2);

        assertEquals(film1, films.get(0), "Первый элемент не совпадает");
        assertEquals(film2, films.get(1), "Второй элемент не совпадает");
        assertEquals(2,films.size(), "Количество элементов не совпадает");


        Film updatedFilm1 = new Film("FilmName1", "UpdatedDescription1", LocalDate.of(1999,9,9), 2000);
        Film updatedFilm2 = new Film("FilmName2", "UpdatedDescription2", LocalDate.of(1998,8,8), 10000);
        updatedFilm1.setId(film1.getId());
        updatedFilm2.setId(film2.getId());
        Film resultFilm1 = inMemoryFilmStorage.put(updatedFilm1);
        Film resultFilm2 = inMemoryFilmStorage.put(updatedFilm2);

        assertEquals(resultFilm1, films.get(0), "Первый элемент не совпадает после обновления");
        assertEquals(resultFilm2, films.get(1), "Второй элемент не совпадает после обновления");
        assertEquals(2,films.size(), "Количество элементов не совпадает после обновления");
    }


    @Test
    public void testAddLike() {
        Film film1 = new Film("FilmName1", "Description1", LocalDate.now(), 1000);
        Film createdFilm1 = inMemoryFilmStorage.create(film1);

        User user1 = new User("e1@mail.ru", "login1", LocalDate.now());
        user1.setName("name1");
        User createdUser1 = inMemoryUserStorage.create(user1);

        inMemoryFilmStorage.addLike(createdFilm1.getId(), createdUser1.getId());

        Set<User> film1Fans = inMemoryFilmStorage.getAllLikes(createdFilm1.getId());

        assertTrue(film1Fans.contains(createdUser1), "Пользователь поставил лайк фильму");
    }

    @Test
    public void testAddLike_shouldThrowEntityAlreadyExistsException() {
        Film film1 = new Film("FilmName1", "Description1", LocalDate.now(), 1000);
        Film createdFilm1 = inMemoryFilmStorage.create(film1);

        User user1 = new User("e1@mail.ru", "login1", LocalDate.now());
        user1.setName("name1");
        User createdUser1 = inMemoryUserStorage.create(user1);

        inMemoryFilmStorage.addLike(createdFilm1.getId(), createdUser1.getId());

        assertThrows(EntityAlreadyExistsException.class, () -> inMemoryFilmStorage.addLike(createdFilm1.getId(), createdUser1.getId()),
                "Пользователь уже поставил лайк фильму");
    }


    @Test
    public void testRemoveLike() {
        Film film1 = new Film("FilmName1", "Description1", LocalDate.now(), 1000);
        Film createdFilm1 = inMemoryFilmStorage.create(film1);

        User user1 = new User("e1@mail.ru", "login1", LocalDate.now());
        user1.setName("name1");
        User createdUser1 = inMemoryUserStorage.create(user1);

        User user2 = new User("e2@mail.ru", "login2", LocalDate.now());
        user2.setName("name2");
        User createdUser2 = inMemoryUserStorage.create(user2);

        inMemoryFilmStorage.addLike(createdFilm1.getId(), createdUser1.getId());
        inMemoryFilmStorage.addLike(createdFilm1.getId(), createdUser2.getId());

        inMemoryFilmStorage.removeLike(createdFilm1.getId(), createdUser1.getId());

        Set<User> film1Fans = inMemoryFilmStorage.getAllLikes(createdFilm1.getId());


        assertFalse(film1Fans.contains(createdUser1), "Пользователь 1 удалил лайк фильму");
        assertTrue(film1Fans.contains(createdUser2), "Пользователь 2 поставил лайк фильму");
    }

    @Test
    public void testRemoveLike_shouldThrowEntityNotFoundException() {
        Film film1 = new Film("FilmName1", "Description1", LocalDate.now(), 1000);
        Film createdFilm1 = inMemoryFilmStorage.create(film1);

        User user1 = new User("e1@mail.ru", "login1", LocalDate.now());
        user1.setName("name1");
        User createdUser1 = inMemoryUserStorage.create(user1);

        User user2 = new User("e2@mail.ru", "login2", LocalDate.now());
        user2.setName("name2");
        User createdUser2 = inMemoryUserStorage.create(user2);

        inMemoryFilmStorage.addLike(createdFilm1.getId(), createdUser1.getId());


        assertThrows(EntityNotFoundException.class, () -> inMemoryFilmStorage.removeLike(createdFilm1.getId(), createdUser2.getId()),
                "Пользователь 2 не ставил лайк фильму");
    }


    @Test
    public void testGetTop10ByLikes() {

        int numberOfFilms = 18;
        List<Film> createdFilms = new ArrayList<>();

        for (int i = 1; i <= numberOfFilms; i++) {
            Film film = new Film("FilmName" + i, "Description" + i, LocalDate.now(), 1000);
            createdFilms.add(inMemoryFilmStorage.create(film));
        }

        int numberOfUsers = 18;
        List<User> createdUsers = new ArrayList<>();

        for (int i = 1; i <= numberOfUsers; i++) {
            User user = new User("e" + i + "@mail.ru", "login" + i, LocalDate.now());
            user.setName("name" + i);
            createdUsers.add(inMemoryUserStorage.create(user));
        }

        for (int i = 0; i < createdFilms.size(); i++) {
            Film film = createdFilms.get(i);
            for (int j = 0; j <= i; j++) {
                User user = createdUsers.get(j);
                inMemoryFilmStorage.addLike(film.getId(), user.getId());
            }
        }

        List<Film> topFilms = inMemoryFilmStorage.getTop10ByLikes(10);


        assertEquals(10, topFilms.size());
        for (int i = 17; i >= 8; i--) {
            assertEquals(createdFilms.get(i), topFilms.get(17 - i), (17 - i + 1) + " элемент не совпадает");
        }

    }


    @Test
    public void testFindFilmByID() {
        Film film1 = new Film("FilmName1", "Description1", LocalDate.now(), 1000);
        Film film2 = new Film("FilmName2", "Description2", LocalDate.now(), 2000);
        Film createdFilm1 = inMemoryFilmStorage.create(film1);
        Film createdFilm2 = inMemoryFilmStorage.create(film2);

        assertEquals(createdFilm1, inMemoryFilmStorage.findFilmByID(createdFilm1.getId()),
                "Фильм 1 найден");
        assertEquals(createdFilm2, inMemoryFilmStorage.findFilmByID(createdFilm2.getId()),
                "Фильм 2 найден");
    }

    @Test
    public void testFindFilmByID_shouldThrowEntityNotFoundException() {
        Film film1 = new Film("FilmName1", "Description1", LocalDate.now(), 1000);
        Film film2 = new Film("FilmName2", "Description2", LocalDate.now(), 2000);

        assertThrows(EntityNotFoundException.class, () -> inMemoryFilmStorage.findFilmByID(film1.getId()),
                "Фильм 1 не найден");
        assertThrows(EntityNotFoundException.class, () -> inMemoryFilmStorage.findFilmByID(film2.getId()),
                "Фильм 2 не найден");
    }

}