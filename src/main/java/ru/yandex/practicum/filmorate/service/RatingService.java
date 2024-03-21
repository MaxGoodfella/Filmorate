package ru.yandex.practicum.filmorate.service;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Rating;

import javax.validation.Valid;
import java.util.List;


public interface RatingService {
    List<Rating> findAll();
    Rating findRatingByID(Integer ratingID);

    Rating save(Rating newRating);

    void saveMany(List<Rating> newRatings);

    boolean deleteById(Integer ratingID);

    boolean deleteAll();
}
