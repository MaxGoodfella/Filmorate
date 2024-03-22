package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;


public interface RatingService {

    Rating save(Rating newRating);

    void saveMany(List<Rating> newRatings);

    List<Rating> findAll();
    Rating findRatingByID(Integer ratingID);

    boolean deleteById(Integer ratingID);

    boolean deleteAll();
}