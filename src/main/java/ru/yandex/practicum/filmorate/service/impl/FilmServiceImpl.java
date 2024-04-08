package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.RatingRepository;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {

    private GenreService genreService;

    private FilmRepository filmRepository;

    private GenreRepository genreRepository;

    private RatingRepository ratingRepository;


    @Override
    public Film save(Film newFilm) {

        Rating existingRating = ratingRepository.findByID(newFilm.getMpa().getId());
        if (existingRating == null) {
            throw new IllegalArgumentException("Rating with id " + newFilm.getMpa().getId() + " does not exist");
        }

        Film savedFilm = filmRepository.save(newFilm);

        List<Genre> genres = newFilm.getGenres();

        if (genres == null || genres.isEmpty()) {
            newFilm.setGenres(new ArrayList<>());
        } else {
            Map<Integer, Genre> allGenresMap = new HashMap<>();
            List<Genre> allGenresList = genreRepository.findAll();
            for (Genre genre : allGenresList) {
                allGenresMap.put(genre.getId(), genre);
            }

            List<Genre> savedGenres = new ArrayList<>();
            Set<Integer> genreIds = new HashSet<>();

            for (Genre genre : genres) {
                Genre existingGenre = allGenresMap.get(genre.getId());
                if (existingGenre == null) {
                    throw new IllegalArgumentException("Genre with id " + genre.getId() + " does not exist");
                }

                if (!genreIds.contains(genre.getId())) {
                    savedGenres.add(existingGenre);
                    genreIds.add(genre.getId());
                }
            }

            genreRepository.add(savedFilm.getId(), savedGenres);
            savedFilm.setGenres(savedGenres);
        }

        return savedFilm;
    }

    @Override
    public Film update(Film film) {
        boolean isSuccess = filmRepository.update(film);

        if (!isSuccess) {
            throw new EntityNotFoundException(Film.class,
                    "Film with id = " + film.getId() + " hasn't been found");
        }

        genreRepository.removeGenresForFilm(film.getId());

        Set<Integer> uniqueGenreIds = new HashSet<>();

        for (Genre genre : film.getGenres()) {
            uniqueGenreIds.add(genre.getId());
        }

        List<Genre> uniqueGenres = new ArrayList<>();
        for (Integer genreId : uniqueGenreIds) {
            Genre genre = new Genre();
            genre.setId(genreId);
            uniqueGenres.add(genre);
        }

        genreRepository.add(film.getId(), uniqueGenres);
        film.setGenres(uniqueGenres);

        return film;
    }


    @Override
    public Film findById(Integer id) {
        try {
            Film film = filmRepository.findById(id);
            genreService.load(List.of(film));
            return film;
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(Film.class, "Film with id = " + id + " hasn't been found");
        }
    }

    @Override
    public Film findByName(String filmName) {
        try {
            Film film = filmRepository.findByName(filmName);
            genreService.load(List.of(film));
            return film;
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(Film.class, "Film with name '" + filmName + "' hasn't been found");
        }
    }

    @Override
    public List<Film> findAll() {
        List<Film> all = filmRepository.findAll();
        genreService.load(all);
        return all;
    }

    @Override
    public boolean deleteById(Integer filmID) {
        return filmRepository.deleteById(filmID);
    }

    @Override
    public boolean deleteAll() {
        return filmRepository.deleteAll();
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {

        Film film = filmRepository.findById(filmId);

        if (film == null) {
            throw new EntityNotFoundException(Film.class, "Film with id = " + filmId + " hasn't been found");
        }

        if (filmRepository.existsLike(filmId, userId)) {
            throw new EntityAlreadyExistsException(Integer.class,
                    "User with id = " + userId + " has already liked film with id = '" + filmId + "'");
        }

        filmRepository.addLike(filmId, userId);

    }

    @Override
    public boolean removeLike(Integer filmId, Integer userId) {

        Film film = filmRepository.findById(filmId);

        if (film == null) {
            throw new EntityNotFoundException(Film.class, "Film with id = " + filmId + " hasn't been found");
        }

        if (!filmRepository.existsLike(filmId, userId)) {
            throw new EntityNotFoundException(Integer.class,
                    "User with id = " + userId + " hasn't liked film with id = '" + filmId + "' yet");
        }

        return filmRepository.removeLike(filmId, userId);

    }

    @Override
    public List<Film> getTopByLikes(Integer count) {
        List<Film> top = filmRepository.getTopByLikes(count);
        genreService.load(top);
        return top;
    }

}