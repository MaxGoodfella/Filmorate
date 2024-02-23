package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {

    Film create(Film film);

    Film put(Film updatedFilm);

    Map<Integer, Film> findAll();

    Film addLike(Integer filmId, Integer userId);

    Film removeLike(Integer filmId, Integer userId);

    List<Film> getTopByLikes(Integer count);

    Film findFilmByID(Integer filmID);

    Set<Long> getAllLikes(Integer filmID);

}