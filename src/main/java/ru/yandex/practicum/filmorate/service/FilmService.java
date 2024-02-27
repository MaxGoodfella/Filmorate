package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface FilmService {

    Film create(Film film);

    Film put(Film updatedFilm);

    List<Film> findAll();

    Film addLike(Integer filmId, Integer userId);

    Film removeLike(Integer filmId, Integer userId);

    List<Film> getTopByLikes(Integer count);

    Film findFilmByID(Integer filmID);

    Set<User> getAllLikes(Integer filmID);

}