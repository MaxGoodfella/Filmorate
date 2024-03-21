package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;


public interface GenreService {

    List<Genre> findAll();
    Genre findGenreByID(Integer genreID);

    Genre save(Genre newGenre);

    void saveMany(List<Genre> newGenres);

    boolean deleteById(Integer genreID);

    boolean deleteAll();

}