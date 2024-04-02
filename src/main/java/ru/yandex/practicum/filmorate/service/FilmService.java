package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    Film save(Film newFilm);

    void saveMany(List<Film> newFilms);

    Film update(Film film);

    Film findById(Integer id);

    Film findByName(String filmName);

    List<Film> findAll();

    boolean deleteById(Integer filmID);

    boolean deleteAll();

    void addLike(Integer filmId, Integer userId);

    boolean removeLike(Integer filmId, Integer userId);

    List<Film> getTopByLikes(Integer count);

}