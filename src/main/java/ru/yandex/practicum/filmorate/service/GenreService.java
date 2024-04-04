package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;


public interface GenreService {

    Genre save(Genre newGenre);

    void update(Genre genre);

    List<Genre> findAll();

    Genre findByID(Integer genreID);

    Genre findByName(String genreName);

    boolean deleteById(Integer genreID);

    boolean deleteAll();

}