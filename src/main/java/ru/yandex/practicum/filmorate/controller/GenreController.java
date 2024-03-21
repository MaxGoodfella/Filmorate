package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.GenreService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private GenreService genreService;

    @GetMapping
    public List<Genre> findAll() {
        return genreService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Genre findGenreByID(@PathVariable("id") Integer genreID) {
        return genreService.findGenreByID(genreID);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Genre save(@Valid @RequestBody Genre newGenre) {
        return genreService.save(newGenre);
    }

    @PostMapping(value = "/multiple", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveMany(@Valid @RequestBody List<Genre> newGenres) {
        genreService.saveMany(newGenres);
    }

    @DeleteMapping(value = "/{id}")
    public boolean deleteById(@PathVariable("id") Integer genreID) {
        return genreService.deleteById(genreID);
    }

    @DeleteMapping
    public boolean deleteAll() {
        return genreService.deleteAll();
    }

}
