package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {

    Film save(Film newFilm);

    void saveMany(List<Film> newFilms);

    boolean update(Film film);

    Film findById(Integer id);

    Film findByName(String filmName);

    Integer findIdByName(String name);

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
