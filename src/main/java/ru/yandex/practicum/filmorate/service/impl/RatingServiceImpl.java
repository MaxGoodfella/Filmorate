package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.RatingRepository;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@AllArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {

    private RatingRepository ratingRepository;

    @Override
    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    @Override
    public Rating findRatingByID(Integer ratingID) {
        return ratingRepository.findRatingByID(ratingID);
    }

    @Override
    public Rating save(Rating newRating) {
        return ratingRepository.save(newRating);
    }

    @Override
    public void saveMany(List<Rating> newRatings) {
        ratingRepository.saveMany(newRatings);
    }

    @Override
    public boolean deleteById(Integer ratingID) {
        return ratingRepository.deleteById(ratingID);
    }

    @Override
    public boolean deleteAll() {
        return ratingRepository.deleteAll();
    }
}
