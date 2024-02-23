package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImplementation implements FilmService {

    private FilmStorage filmStorage;
    private UserStorage userStorage;

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
        userStorage.findUserByID(userId);
        return filmStorage.addLike(filmId, userId);
    }

    @Override
    public Film removeLike(Integer filmId, Integer userId) {
        userStorage.findUserByID(userId);
        return filmStorage.removeLike(filmId, userId);
    }

    @Override
    public List<Film> getTopByLikes(Integer count) {
        return filmStorage.getTopByLikes(count);
    }

    @Override
    public Film findFilmByID(Integer filmID) {
        return filmStorage.findFilmByID(filmID);
    }

    @Override
    public Set<User> getAllLikes(Integer filmID) {
        Set<Integer> fansIds = filmStorage.getAllLikes(filmID);
        Set<User> filmFans = new HashSet<>();

        for (Integer userId : fansIds) {
            User user = userStorage.findUserByID(userId);
            filmFans.add(user);
        }
        return filmFans;
    }

}