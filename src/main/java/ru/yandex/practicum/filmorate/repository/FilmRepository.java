package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    Film save(Film newFilm);

    void saveMany(List<Film> newFilms);

    Film findById(Integer id);

    List<Film> findAll();

    boolean deleteById(Integer filmID);

    boolean deleteAll();

// save
// delete
    // update film


    // add genre to film
    // remove genre from film

    // add rating to film
    // remove rating from film

    // add like (update film_fans) + count popularity for film_id by user_id amount


}
