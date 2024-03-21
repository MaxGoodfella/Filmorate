package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface RatingRepository {

// вроде все сделал

    List<Rating> findAll();

    Rating findRatingByID(Integer ratingID);

    Rating save(Rating newRating);

    void saveMany(List<Rating> newRatings);

    boolean deleteById(Integer ratingID);

    boolean deleteAll();

}
