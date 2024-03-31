package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.List;

public interface FilmRepository {

    Film save(Film newFilm);

    //void saveMany(List<Film> newFilms);

    boolean update(Film film);

    Film findById(Integer id);

    Film findByName(String filmName);

    Integer findIdByName(String name);

    Film findByNameDescriptionReleaseDateAndDuration(String name, String description,
                                                     LocalDate releaseDate, int duration);

    List<Film> findAll();

    boolean deleteById(Integer filmID);

    boolean deleteAll();

    void addLike(Integer filmId, Integer userId);

    boolean removeLike(Integer filmId, Integer userId);

    List<Integer> findFansIds(Integer filmId);

    boolean existsLike(Integer filmId, Integer userId);

    List<Film> getTopByLikes(Integer count);

//    //void addGenres(Integer filmId, List<Integer> genreIds);
//    List<Genre> addGenres(Integer filmId, List<Genre> genres);
//
//    // boolean removeGenre(Integer filmId, String genreName);
//    boolean removeGenre(Integer filmId, Integer genreId);
//
//    // List<String> findGenresNames(Integer filmId);
//
//
//    List<Genre> findGenresForFilm(Integer filmId);
//
//    List<Integer> findGenresIdsForFilm(Integer filmId);
//
//    boolean existsGenre(Integer filmId, Integer genreId);

}
