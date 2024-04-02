package ru.yandex.practicum.filmorate.service.impl;

import com.sun.source.tree.LabeledStatementTree;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {

    private FilmRepository filmRepository;

    private GenreRepository genreRepository;

    private RatingRepository ratingRepository;

//        Film existingFilm = filmRepository.findByNameDescriptionReleaseDateAndDuration(
//                newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(), newFilm.getDuration());
//
//        //List<Genre> existingGenres = genreRepository.findGenresForFilm(existingFilm.getId());
//
//        //if (existingFilm != null && existingGenres != null) {
//        if (existingFilm != null) {
//            throw new EntityAlreadyExistsException(Film.class, "Фильм с такими параметрами уже существует");
//        }
//
//        return filmRepository.save(newFilm);

//    @Override
//    public Film save(Film newFilm) {
//
//
//        Film existingFilm = filmRepository.findByNameDescriptionReleaseDateAndDuration(
//                newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(), newFilm.getDuration());
//
//        if (existingFilm != null) {
//
//            // использовать метод нахождения рейтинга по filmid
//
////            Rating existingRating = ratingRepository.findByFilmId(existingFilm.getId());
////
////            if (existingRating != null) {
////                throw new EntityAlreadyExistsException(Film.class, "Фильм с такими параметрами уже существует");
////            }
//
//            List<Genre> existingGenres = genreRepository.findGenresForFilm(existingFilm.getId());
//            if (existingGenres != null) {
//                throw new EntityAlreadyExistsException(Film.class, "Фильм с такими параметрами уже существует");
//            }
//        }
//
//        return filmRepository.save(newFilm);
//
//    }


    @Override
    public Film save(Film newFilm) {
        Film existingFilm = filmRepository.findByNameDescriptionReleaseDateAndDuration(
                newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(), newFilm.getDuration());

        if (existingFilm != null) {
            Rating existingRating = ratingRepository.findByFilmId(existingFilm.getId());
            if (existingRating != null && existingRating.getId() == newFilm.getMpa().getId()) {
                List<Genre> existingGenres = genreRepository.findGenresForFilm(existingFilm.getId());
                if (existingGenres != null) {
                    for (Genre existingGenre : existingGenres) {
                        for (Genre newGenre : newFilm.getGenres()) {
                            if (existingGenre.getId().equals(newGenre.getId())) {
                                throw new EntityAlreadyExistsException(Film.class, "Фильм с такими параметрами уже существует");
                            }
                        }
                    }
                }
            }
        }

        return filmRepository.save(newFilm);
    }



//    @Override
//    public void saveMany(List<Film> newFilms) {
//
//        List<Film> existingFilms = new ArrayList<>();
//        List<Film> newFilmsToSave = new ArrayList<>();
//
//
//        for (Film newFilm : newFilms) {
//
//            Film existingFilm = filmRepository.findByNameDescriptionReleaseDateAndDuration(
//                    newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(), newFilm.getDuration());
//
//            if (existingFilm != null) {
//                existingFilms.add(existingFilm);
//            } else {
//                newFilmsToSave.add(newFilm);
//            }
//
//        }
//
//        if (!newFilmsToSave.isEmpty()) {
//            filmRepository.saveMany(newFilmsToSave);
//        }
//
//    }

    @Override
    public Film update(Film film) {
        boolean isSuccess = filmRepository.update(film);

        if (!isSuccess) {
            throw new EntityNotFoundException(Film.class,
                    "Film with id = " + film.getId() + " hasn't been found");
        }

        return film;
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

    } // плохие сообщения выдает, но хотя бы нужный код ответа


    @Override
    public List<Film> getTopByLikes(Integer count) {
        return filmRepository.getTopByLikes(count);
    }

////    @Override
////    public List<Genre> addGenres(Integer filmId, List<Genre> genres) {
////
////        Film film = filmRepository.findById(filmId);
////
////        if (film == null) {
////            throw new EntityNotFoundException(Film.class, "Film with id = " + filmId + " hasn't been found");
////        }
////
////        for (Integer genreId : genreIds) {
////            if (filmRepository.existsLike(filmId, genreId)) {
////                throw new EntityAlreadyExistsException(Integer.class,
////                        "Film with id = " + filmId + " already has genre with id = " + genreId);
////            }
////        }
////
////        filmRepository.addGenres(filmId, genreIds);
////
////    }
//
////    @Override
////    public boolean removeGenre(Integer filmId, String genreName) {
////
////        Film film = filmRepository.findById(filmId);
////
////        if (film == null) {
////            throw new EntityNotFoundException(Film.class, "Film with id = " + filmId + " hasn't been found");
////        }
////
////        if (!filmRepository.findGenresForFilm(filmId).contains(genreName)) {
////            throw new EntityNotFoundException(String.class,
////                        "Film with id = " + filmId + " hasn't got genre with name = " + genreName + "' yet");
////        }
////
////        return filmRepository.removeGenre(filmId, genreName);
////
////    }
//
//    @Override
//    public boolean removeGenre(Integer filmId, Integer genreId) {
//
//        Film film = filmRepository.findById(filmId);
//
//        if (film == null) {
//            throw new EntityNotFoundException(Film.class, "Film with id = " + filmId + " hasn't been found");
//        }
//
////        if (!filmRepository.findGenresForFilm(filmId).contains(genreName)) {
////            throw new EntityNotFoundException(String.class,
////                    "Film with id = " + filmId + " hasn't got genre with name = " + genreName + "' yet");
////        }
//
//        if (!filmRepository.findGenresIdsForFilm(filmId).contains(genreId)) {
//            throw new EntityNotFoundException(String.class,
//                    "Film with id = " + filmId + " hasn't got genre with id = " + genreId + "' yet");
//        }
//
//        return filmRepository.removeGenre(filmId, genreId);
//
//    }
//
////    @Override
////    public List<String> findGenresNames(Integer filmId) {
////
////        Film film = filmRepository.findById(filmId);
////
////        if (film == null) {
////            throw new EntityNotFoundException(Film.class, "Film with id = " + filmId + " hasn't been found");
////        }
////
////        return filmRepository.findGenresNames(filmId);
////    }
//
//    @Override
//    public List<Genre> findGenresForFilm(Integer filmId) {
//
//        Film film = filmRepository.findById(filmId);
//
//        if (film == null) {
//            throw new EntityNotFoundException(Film.class, "Film with id = " + filmId + " hasn't been found");
//        }
//
//        return filmRepository.findGenresForFilm(filmId);
//    }

}