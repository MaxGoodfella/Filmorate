package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {

    private FilmRepository filmRepository;


    @Override
    public Film save(Film newFilm) {
        String filmName = newFilm.getName();
        Integer existingFilmId = filmRepository.findIdByName(filmName);
        if (existingFilmId != null) {
            newFilm.setId(existingFilmId);
            return newFilm;
        } else {
            return filmRepository.save(newFilm);
        }
    }

    @Override
    public void saveMany(List<Film> newFilms) {
        for (Film newFilm : newFilms) {
            Integer existingFilmId = filmRepository.findIdByName(newFilm.getName());
            if (existingFilmId != null) {
                newFilm.setId(existingFilmId);
                update(newFilm);
                System.out.println("Film '" + newFilm.getName() + "' has been updated.");
            } else {
                save(newFilm);
                System.out.println("Film '" + newFilm.getName() + "' has been added.");
            }
        }
    }

    @Override
    public void update(Film film) {
        boolean isSuccess = filmRepository.update(film);

        if (!isSuccess) {
            throw new EntityNotFoundException(Film.class,
                    "Film with id = " + film.getId() + " hasn't been found");
        }
    }

    @Override
    public Film findById(Integer id) {
        try {
            return filmRepository.findById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(Film.class, "Film with id = " + id + " hasn't been found");
        }
    }

    @Override
    public Film findByName(String filmName) {
        try {
            return filmRepository.findByName(filmName);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(Film.class, "Film with name '" + filmName + "' hasn't been found");
        }
    }

    @Override
    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    @Override
    public boolean deleteById(Integer filmID) {
        return filmRepository.deleteById(filmID);
    }

    @Override
    public boolean deleteAll() {
        return filmRepository.deleteAll();
    }







//    private FilmStorage filmStorage;
//    private UserStorage userStorage;
//
//    @Override
//    public Film create(Film film) {
//        return filmStorage.create(film);
//    }
//
//    @Override
//    public Film put(Film updatedFilm) {
//        return filmStorage.put(updatedFilm);
//    }
//
//    @Override
//    public List<Film> findAll() {
//        return filmStorage.findAll();
//    }
//
//    @Override
//    public Film addLike(Integer filmId, Integer userId) {
//        userStorage.findUserByID(userId);
//        return filmStorage.addLike(filmId, userId);
//    }
//
//    @Override
//    public Film removeLike(Integer filmId, Integer userId) {
//        userStorage.findUserByID(userId);
//        return filmStorage.removeLike(filmId, userId);
//    }
//
//    @Override
//    public List<Film> getTopByLikes(Integer count) {
//        return filmStorage.getTopByLikes(count);
//    }
//
//    @Override
//    public Film findFilmByID(Integer filmID) {
//        return filmStorage.findFilmByID(filmID);
//    }
//
//    @Override
//    public Set<User> getAllLikes(Integer filmID) {
//        Set<Integer> fansIds = filmStorage.getAllLikes(filmID);
//        Set<User> filmFans = new HashSet<>();
//
//        for (Integer userId : fansIds) {
//            User user = userStorage.findUserByID(userId);
//            filmFans.add(user);
//        }
//        return filmFans;
//    }

}