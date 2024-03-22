package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreRepository {

    Genre save(Genre newGenre);

    void saveMany(List<Genre> newGenres);

    List<Genre> findAll();

    Genre findGenreByID(Integer genreID);

    boolean deleteById(Integer genreID);

    boolean deleteAll();

}