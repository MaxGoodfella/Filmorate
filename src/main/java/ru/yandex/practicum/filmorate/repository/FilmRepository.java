package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

public interface FilmRepository {

    Film save(Film newFilm);

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

}