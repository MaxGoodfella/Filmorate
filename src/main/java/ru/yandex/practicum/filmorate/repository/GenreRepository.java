package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreRepository {

    Genre save(Genre newGenre);

    void saveMany(List<Genre> newGenres);

    boolean update(Genre genre);

    Genre findGenreByID(Integer genreID);

    Genre findByName(String genreName);

    Integer findIdByName(String name);

    List<Genre> findAll();

    boolean deleteById(Integer genreID);

    boolean deleteAll();

}