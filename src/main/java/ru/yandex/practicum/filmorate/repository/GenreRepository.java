package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreRepository {

    // add
    // delete
    // get all
    // get by id

    List<Genre> findAll();
    Genre findGenreByID(Integer genreID);

    Genre save(Genre newGenre);

    void saveMany(List<Genre> newGenres);

    boolean deleteById(Integer genreID);

    boolean deleteAll();

}
