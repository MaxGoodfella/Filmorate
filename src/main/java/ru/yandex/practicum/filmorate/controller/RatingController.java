package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
@RequestMapping("/mpa")
public class RatingController {

    private RatingService ratingService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Rating save(@Valid @RequestBody Rating newRating) {
        log.info("Start saving rating {}", newRating);
        Rating savedRating = ratingService.save(newRating);
        log.info("Finish saving rating {}", savedRating);
        return savedRating;
    }

    @PostMapping(value = "/multiple", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveMany(@Valid @RequestBody List<Rating> newRatings) {
        log.info("Start saving ratings {}", newRatings);
        ratingService.saveMany(newRatings);
        log.info("Finish saving ratings {}", newRatings);
    }

    @PutMapping
    public void update(@Valid @RequestBody Rating rating) {
        log.info("Start updating rating {}", rating);
        ratingService.update(rating);
        log.info("Finish updating rating {}", rating);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Rating findRatingByID(@PathVariable("id") Integer ratingID) {
        log.info("Start fetching rating with id = {}", ratingID);
        Rating fetchedRating = ratingService.findRatingByID(ratingID);
        log.info("Finish fetching rating with id = {}", fetchedRating.getId());
        return fetchedRating;
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Rating findGenreByName(@PathVariable("name") String ratingName) {
        log.info("Start fetching rating with name = {}", ratingName);
        Rating fetchedRating = ratingService.findByName(ratingName);
        log.info("Finish fetching rating with name = {}", ratingName);
        return fetchedRating;
    }

    @GetMapping
    public List<Rating> findAll() {
        log.info("Start fetching all ratings");
        List<Rating> fetchedRatings = ratingService.findAll();
        log.info("Finish fetching all ratings");
        return fetchedRatings;
    }

    @DeleteMapping(value = "/{id}")
    public boolean deleteById(@PathVariable("id") Integer ratingID) {
        log.info("Start deleting rating with id = {}", ratingID);
        boolean isDeleted = ratingService.deleteById(ratingID);
        log.info("Finish deleting rating with id = {}", ratingID);
        return isDeleted;
    }

    @DeleteMapping
    public boolean deleteAll() {
        log.info("Start deleting all ratings");
        boolean areDeleted = ratingService.deleteAll();
        log.info("Finish deleting all ratings");
        return areDeleted;
    }

}