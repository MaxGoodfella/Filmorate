package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
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


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Genre save(@Valid @RequestBody Genre newGenre) {
        log.info("Start saving genre {}", newGenre);
        Genre savedGenre = genreService.save(newGenre);
        log.info("Finish saving genre {}", savedGenre);
        return savedGenre;
    }

    @PostMapping(value = "/multiple", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveMany(@Valid @RequestBody List<Genre> newGenres) {
        log.info("Start saving genres {}", newGenres);
        genreService.saveMany(newGenres);
        log.info("Finish saving genres {}", newGenres);
    }

    @PutMapping
    public void update(@Valid @RequestBody Genre genre) {
        log.info("Start updating genre {}", genre);
        genreService.update(genre);
        log.info("Finish updating genre {}", genre);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Genre findByID(@PathVariable("id") Integer genreID) {
        log.info("Start fetching genre with id = {}", genreID);
        Genre fetchedGenre = genreService.findByID(genreID);
        log.info("Finish fetching genre with id = {}", fetchedGenre.getId());
        return fetchedGenre;
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Genre findByName(@PathVariable("name") String genreName) {
        log.info("Start fetching genre with name = {}", genreName);
        Genre fetchedGenre = genreService.findByName(genreName);
        log.info("Finish fetching genre with name = {}", genreName);
        return fetchedGenre;
    }

    @GetMapping
    public List<Genre> findAll() {
        log.info("Start fetching all genres");
        List<Genre> fetchedFilms = genreService.findAll();
        log.info("Finish fetching all genres");
        return fetchedFilms;
    }


    @DeleteMapping(value = "/{id}")
    public boolean deleteById(@PathVariable("id") Integer genreID) {
        log.info("Start deleting genre with id = {}", genreID);
        boolean isDeleted = genreService.deleteById(genreID);
        log.info("Finish deleting genre with id = {}", genreID);
        return isDeleted;
    }

    @DeleteMapping
    public boolean deleteAll() {
        log.info("Start deleting all genres");
        boolean areDeleted = genreService.deleteAll();
        log.info("Finish deleting all genres");
        return areDeleted;
    }

}