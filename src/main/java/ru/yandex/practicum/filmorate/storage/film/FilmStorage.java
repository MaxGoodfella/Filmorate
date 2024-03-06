package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {

    Film create(Film film);

    Film put(Film updatedFilm);

    List<Film> findAll();

    Film addLike(Integer filmId, Integer userId);

    Film removeLike(Integer filmId, Integer userId);

    List<Film> getTopByLikes(Integer count);

    Film findFilmByID(Integer filmID);

    Set<Integer> getAllLikes(Integer filmID);

}