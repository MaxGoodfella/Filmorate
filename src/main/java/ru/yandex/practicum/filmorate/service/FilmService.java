package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    Film create(Film film);

    Film put(Film updatedFilm);

    List<Film> findAll();

    Film addLike(Integer filmId, Integer userId);

    Film removeLike(Integer filmId, Integer userId);

    List<Film> getTop10ByLikes(Integer count);

    Film findFilmByID(Integer filmID);

}