package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Film findFilmByID(@PathVariable("id") Integer filmID) {
        return filmService.findById(filmID);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Film save(@Valid @RequestBody Film newFilm) {
        return filmService.save(newFilm);
    }

    @DeleteMapping(value = "/{id}")
    public boolean deleteById(@PathVariable("id") Integer filmID) {
        return filmService.deleteById(filmID);
    }

    @DeleteMapping
    public boolean deleteAll() {
        return filmService.deleteAll();
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