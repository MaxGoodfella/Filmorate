package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
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


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Film findFilmByID(@PathVariable("id") Integer filmID) {
        log.info("Start fetching film with id = {}", filmID);
        Film fetchedFilm = filmService.findById(filmID);
        log.info("Finish fetching film with id = {}", fetchedFilm.getId());
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



//    private FilmService filmService;
//
//
//    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public Film create(@Valid @RequestBody Film film) {
//        return filmService.create(film);
//    }
//
//    @PutMapping
//    public Film put(@Valid @RequestBody Film updatedFilm) {
//        return filmService.put(updatedFilm);
//    }
//
//    @GetMapping
//    public List<Film> findAll() {
//        return filmService.findAll();
//    }
//
//    @PutMapping("/{id}/like/{userId}")
//    public Film addLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
//        return filmService.addLike(filmId, userId);
//    }
//
//    @DeleteMapping("/{id}/like/{userId}")
//    public Film removeLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
//        return filmService.removeLike(filmId, userId);
//    }
//
//    @GetMapping(value = "/popular", produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<Film> getTopByLikes(@RequestParam(defaultValue = "10")  @Positive Integer count) {
//        return filmService.getTopByLikes(count);
//    }
//
//    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Film findFilmByID(@PathVariable("id") Integer filmID) {
//        return filmService.findFilmByID(filmID);
//    }

}