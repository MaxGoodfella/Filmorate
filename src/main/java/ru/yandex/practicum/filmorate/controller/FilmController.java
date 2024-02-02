package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final List<Film> films = new ArrayList<>();

    protected int generatedID = 0;


    private int generateID() {
        return ++generatedID;
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Film create(@Valid @RequestBody Film film) {

        for (Film existingFilm : films) {
            if (existingFilm.getName().equals(film.getName()) &&
                    existingFilm.getDescription().equals(film.getDescription()) &&
                    existingFilm.getDuration().equals(film.getDuration()) &&
                    existingFilm.getReleaseDate().equals(film.getReleaseDate())) {
                log.warn("Регистрация фильма не удалась. Фильм с названием {}, описанием {}, продолжительностью {} " +
                        "и датой выхода {} уже существует.",
                        film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate());
                throw new EntityAlreadyExistsException(Film.class, "Фильм с названием " + film.getName() + " уже зарегистрирован.");
            }
        }

        film.setId(generateID());

        films.add(film);
        log.info("Фильм успешно зарегистрирован. Название: {}", film.getName());
        return film;

    }

    @PutMapping
    public Film put(@Valid @RequestBody Film updatedFilm) {

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
        throw new EntityNotFoundException(Film.class, "Фильм с id " + idToUpdate + " не найден.");

    }

    @GetMapping
    public List<Film> findAll() {
        return films;
    }

}