package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private FilmService filmService;


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Film save(@Valid @RequestBody Film newFilm) {
        log.info("Start saving film {}", newFilm);
        Film savedFilm = filmService.save(newFilm);
        log.info("Finish saving film {}", savedFilm);
        return savedFilm;
    }

    @PostMapping(value = "/multiple", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveMany(@Valid @RequestBody List<Film> newFilms) {
        log.info("Start saving films {}", newFilms);
        filmService.saveMany(newFilms);
        log.info("Finish saving films {}", newFilms);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Start updating film {}", film);
        Film savedFilm = filmService.update(film);
        log.info("Finish updating film {}", film);
        return savedFilm;
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Film findByID(@PathVariable("id") Integer filmID) {
        log.info("Start fetching film with id = {}", filmID);
        Film fetchedFilm = filmService.findById(filmID);
        log.info("Finish fetching film with id = {}", fetchedFilm.getId());
        return fetchedFilm;
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Film findByName(@PathVariable("name") String filmName) {
        log.info("Start fetching film with name = {}", filmName);
        Film fetchedFilm = filmService.findByName(filmName);
        log.info("Finish fetching film with name = {}", filmName);
        return fetchedFilm;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Film> findAll() {
        log.info("Start fetching all films");
        List<Film> fetchedFilms = filmService.findAll();
        log.info("Finish fetching all films");
        return fetchedFilms;
    }

    @DeleteMapping(value = "/{id}")
    public boolean deleteById(@PathVariable("id") Integer filmID) {
        log.info("Start deleting film with id = {}", filmID);
        boolean isDeleted = filmService.deleteById(filmID);
        log.info("Finish deleting film with id = {}", filmID);
        return isDeleted;
    }

    @DeleteMapping
    public boolean deleteAll() {
        log.info("Start deleting all films");
        boolean areDeleted = filmService.deleteAll();
        log.info("Finish deleting all films");
        return areDeleted;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.info("Start adding like to film with id = {} from user with id = {}", filmId, userId);
        filmService.addLike(filmId, userId);
        log.info("Finish adding like to film with id = {} from user with id = {}", filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean removeLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.info("Start removing like to film with id = {} from user with id = {}", filmId, userId);
        boolean isRemoved = filmService.removeLike(filmId, userId);
        log.info("Finish removing like to film with id = {} from user with id = {}", filmId, userId);
        return isRemoved;
    }

    @GetMapping(value = "/popular", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Film> getTopByLikes(@RequestParam(defaultValue = "10")  @Positive Integer count) {
        log.info("Start fetching {} top films by likes", count);
        List<Film> topFilms = filmService.getTopByLikes(count);
        log.info("Finish fetching {} top films by likes", count);
        return topFilms;
    }

}