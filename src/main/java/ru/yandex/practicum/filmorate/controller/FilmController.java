package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Logger log = LoggerFactory.getLogger(FilmController.class);

    private final List<Film> films = new ArrayList<>();

    protected int generatedID = 0;


    private int generateID() {
        return ++generatedID;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Film create(@RequestBody Film film) {

        validateFilm(film);

        for (Film existingFilm : films) {
            if (existingFilm.getName().equals(film.getName()) &&
                    existingFilm.getDescription().equals(film.getDescription()) &&
                    existingFilm.getDuration().equals(film.getDuration()) &&
                    existingFilm.getReleaseDate().equals(film.getReleaseDate())) {
                log.warn("Регистрация фильма не удалась. Фильм с названием {}, описанием {}, продолжительностью {} " +
                        "и датой выхода {} уже существует.",
                        film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate());
                throw new FilmAlreadyExistsException("Фильм с названием " + film.getName() + " уже зарегистрирован.");
            }
        }

        film.setId(generateID());

        films.add(film);
        log.info("Фильм успешно зарегистрирован. Название: {}", film.getName());
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film updatedFilm) {
        validateFilm(updatedFilm);

        int idToUpdate = updatedFilm.getId();

         for (Film film : films) {
            if (film.getId() == idToUpdate) {
                film.setDescription(updatedFilm.getDescription());
                film.setName(updatedFilm.getName());
                film.setReleaseDate(updatedFilm.getReleaseDate());
                film.setDuration(updatedFilm.getDuration());

                log.info("Фильм c id {} успешно обновлен. Название: {}", idToUpdate, updatedFilm.getName());
                return film;
            }
        }
        log.warn("Фильм c id {} для обновления не найден.", idToUpdate);
        throw new FilmNotFoundException("Фильм с id " + idToUpdate + " не найден.");
    }

    @GetMapping
    public List<Film> findAll() {
        return films;
    }


    private void validateFilm(Film film) {
        if (film.getName().isEmpty()) {
            log.error("Название фильма не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым.");
        }

        if (film.getDescription().length() > 0 && film.getDescription().length() > 200) {
            log.error("Максимальная длина описания фильма — 200 символов. Сейчас {}.", film.getDescription().length());
            throw new ValidationException("Максимальная длина описания фильма — 200 символов.");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза не может быть раньше 28 декабря 1895 года. Сейчас {}.", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }

}
