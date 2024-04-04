package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreRepository {

    Genre save(Genre newGenre);

    boolean update(Genre genre);

    Genre findByID(Integer genreID);

    Genre findByName(String genreName);

    Integer findIdByName(String name);

    List<Genre> findAll();

    boolean deleteById(Integer genreID);

    boolean deleteAll();

    List<Genre> add(Integer filmId, List<Genre> genres);

    List<Genre> findGenresForFilm(Integer filmId);

}