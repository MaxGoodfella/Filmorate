package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface RatingRepository {

    Rating save(Rating newRating);

    void saveMany(List<Rating> newRatings);

    boolean update(Rating rating);

    List<Rating> findAll();

    Rating findByID(Integer ratingID);

    Rating findByName(String ratingName);

    Integer findIdByName(String name);

    boolean deleteById(Integer ratingID);

    boolean deleteAll();

}