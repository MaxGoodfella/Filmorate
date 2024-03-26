package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    Film save(Film newFilm);

    void saveMany(List<Film> newFilms);

    void update(Film film);

    Film findById(Integer id);

    Film findByName(String filmName);

    List<Film> findAll();

    boolean deleteById(Integer filmID);

    boolean deleteAll();





//
//    Film addLike(Integer filmId, Integer userId);
//
//    Film removeLike(Integer filmId, Integer userId);
//
//    List<Film> getTopByLikes(Integer count);
//
//    Set<User> getAllLikes(Integer filmID);

}