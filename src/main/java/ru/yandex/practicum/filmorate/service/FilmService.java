package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmService {

    Film save(Film newFilm);

    // void saveMany(List<Film> newFilms);

    // void update(Film film);
    Film update(Film film);

    Film findById(Integer id);

    Film findByName(String filmName);

    List<Film> findAll();

    boolean deleteById(Integer filmID);

    boolean deleteAll();

    void addLike(Integer filmId, Integer userId);

    boolean removeLike(Integer filmId, Integer userId);

    List<Film> getTopByLikes(Integer count);

//    // void addGenres(Integer filmId, List<Integer> genreIds);
////    List<Genre> addGenres(Integer filmId, List<Genre> genres);
//
//    // boolean removeGenre(Integer filmId, String genreName);
//    boolean removeGenre(Integer filmId, Integer genreId);
//
//    //List<String> findGenresNames(Integer filmId);
//
//    List<Genre> findGenresForFilm(Integer filmId);

}