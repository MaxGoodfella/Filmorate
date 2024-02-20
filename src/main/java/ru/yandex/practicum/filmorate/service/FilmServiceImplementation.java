package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImplementation implements FilmService {

    private FilmStorage filmStorage;

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film put(Film updatedFilm) {
        return filmStorage.put(updatedFilm);
    }

    @Override
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        return filmStorage.addLike(filmId, userId);
    }

    @Override
    public Film removeLike(Integer filmId, Integer userId) {
        return filmStorage.removeLike(filmId, userId);
    }

    @Override
    public List<Film> getTop10ByLikes(Integer count) {
        return filmStorage.getTop10ByLikes(count);
    }

    @Override
    public Film findFilmByID(Integer filmID) {
        return filmStorage.findFilmByID(filmID);
    }

}