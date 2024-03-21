package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
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

    @GetMapping
    public List<Rating> findAll() {
        return ratingService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Rating findRatingByID(@PathVariable("id") Integer ratingID) {
        return ratingService.findRatingByID(ratingID);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Rating save(@Valid @RequestBody Rating newRating) {
        return ratingService.save(newRating);
    }

    @PostMapping(value = "/multiple", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveMany(@Valid @RequestBody List<Rating> newRatings) {
        ratingService.saveMany(newRatings);
    }

    @DeleteMapping(value = "/{id}")
    public boolean deleteById(@PathVariable("id") Integer ratingID) {
        return ratingService.deleteById(ratingID);
    }

    @DeleteMapping
    public boolean deleteAll() {
        return ratingService.deleteAll();
    }

}
