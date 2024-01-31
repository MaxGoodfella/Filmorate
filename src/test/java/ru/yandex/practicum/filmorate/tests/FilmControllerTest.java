package ru.yandex.practicum.filmorate.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class FilmControllerTest {

    private static FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
    }

    @Test
    public void testCreateFilmWithValidParameters_shouldNotThrowValidationException() {
        Film film = new Film("FilmName", "Description", LocalDate.now(), 1000);

        assertDoesNotThrow(() -> filmController.create(film));
    }

    @Test
    public void testCreateSameFilm_shouldThrowFilmAlreadyExistsException() {
        Film film1 = new Film("FilmName", "Description", LocalDate.now(), 1000);
        Film film2 = new Film("FilmName", "Description", LocalDate.now(), 1000);

        filmController.create(film1);
        assertThrows(FilmAlreadyExistsException.class, () -> filmController.create(film2));
    }

    @Test
    public void testCreateFilmWithEmptyName_shouldThrowValidationException() {
        Film film = new Film("", "Description", LocalDate.now(), 1000);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    public void testCreateFilmWithNegativeDuration_shouldThrowValidationException() {
        Film film = new Film("FilmName", "Description", LocalDate.now(), -1);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    public void testCreateFilmWithZeroDuration_shouldThrowValidationException() {
        Film film = new Film("FilmName", "Description", LocalDate.now(), 0);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void testCreateFilmWithInvalidDescription_shouldThrowValidationException() {
        String invalidDescription = "The description that is much too long to be accepted by the service. " +
                "Next time try to come up with something shorter. " +
                "I have no idea what else to sssssssssssssssssssssssssaaaaaaaaaaaaaaaaaaaaaaaaaay here";

        Film film = new Film("FilmName", invalidDescription, LocalDate.now(), 1000);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void testCreateFilmWithInvalidReleaseDate_shouldThrowValidationException() {
        LocalDate releaseDateBefore18951228 = LocalDate.of(1895, 12, 27);

        Film film = new Film("FilmName", "Description", releaseDateBefore18951228, 1000);

        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void testCreateFilmWithReleaseDate18951228_shouldNotThrowValidationException() {
        LocalDate releaseDate18951228 = LocalDate.of(1895, 12, 28);

        Film film = new Film("FilmName", "Description", releaseDate18951228, 1000);

        assertDoesNotThrow(() -> filmController.create(film));
    }


    @Test
    public void testPutUpdateExistingFilm() {
        Film initialFilm = new Film("FilmName", "Description", LocalDate.now(), 1000);
        filmController.create(initialFilm);

        Film updatedFilm = new Film("FilmName", "UpdatedDescription", LocalDate.of(1999,9,9), 2000);
        updatedFilm.setId(initialFilm.getId());
        Film resultFilm = filmController.put(updatedFilm);

        assertEquals(updatedFilm, resultFilm, "Фильм должен быть обновлен");
        assertEquals("UpdatedDescription", resultFilm.getDescription(), "Описание фильма должно быть обновлено");
    }


    @Test
    public void testPutFilmWithInvalidName_shouldThrowValidationException() {
        Film film = new Film("", "Description", LocalDate.now(), 1000);

        assertThrows(ValidationException.class, () -> filmController.put(film));
    }

    @Test
    public void testPutFilmWithInvalidDescription_shouldThrowValidationException() {
        String invalidDescription = "The description that is much too long to be accepted by the service. " +
                "Next time try to come up with something shorter. " +
                "I have no idea what else to sssssssssssssssssssssssssaaaaaaaaaaaaaaaaaaaaaaaaaay here";

        Film film = new Film("FilmName", invalidDescription, LocalDate.now(), 1000);

        assertThrows(ValidationException.class, () -> filmController.put(film));
    }

    @Test
    public void testPutFilmWithInvalidReleaseDate_shouldThrowValidationException() {
        Film film = new Film("FilmName", "Description", LocalDate.of(1895,12,27), 1000);

        assertThrows(ValidationException.class, () -> filmController.put(film));
    }

    @Test
    public void testPutFilmWithInvalidDuration_shouldThrowValidationException() {
        Film film = new Film("FilmName", "Description", LocalDate.now(), 0);

        assertThrows(ValidationException.class, () -> filmController.put(film));
    }


    @Test
    public void testFindAll() {
        List<Film> films = filmController.findAll();

        Film film1 = new Film("FilmName1", "Description1", LocalDate.now(), 1000);
        Film film2 = new Film("FilmName2", "Description2", LocalDate.now(), 2000);
        filmController.create(film1);
        filmController.create(film2);

        assertEquals(film1, films.get(0), "Первый элемент не совпадает");
        assertEquals(film2, films.get(1), "Второй элемент не совпадает");
        assertEquals(2,films.size(), "Количество элементов не совпадает");


        Film updatedFilm1 = new Film("FilmName1", "UpdatedDescription1", LocalDate.of(1999,9,9), 2000);
        Film updatedFilm2 = new Film("FilmName2", "UpdatedDescription2", LocalDate.of(1998,8,8), 10000);
        updatedFilm1.setId(film1.getId());
        updatedFilm2.setId(film2.getId());
        Film resultFilm1 = filmController.put(updatedFilm1);
        Film resultFilm2 = filmController.put(updatedFilm2);

        assertEquals(resultFilm1, films.get(0), "Первый элемент не совпадает после обновления");
        assertEquals(resultFilm2, films.get(1), "Второй элемент не совпадает после обновления");
        assertEquals(2,films.size(), "Количество элементов не совпадает после обновления");
    }

}