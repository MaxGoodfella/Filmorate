package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private GenreRepository genreRepository;


    @Override
    public Genre save(Genre genre) {
        String genreName = genre.getName();
        Integer existingGenreId = genreRepository.findIdByName(genreName);
        if (existingGenreId != null) {
            genre.setId(existingGenreId);
            return genre;
        } else {
            return genreRepository.save(genre);
        }
    }

    @Override
    public void saveMany(List<Genre> newGenres) {
        for (Genre newGenre : newGenres) {
            Integer existingGenreId = genreRepository.findIdByName(newGenre.getName());
            if (existingGenreId != null) {
                newGenre.setId(existingGenreId);
                update(newGenre);
                System.out.println("Genre '" + newGenre.getName() + "' has been updated.");
            } else {
                save(newGenre);
                System.out.println("Genre '" + newGenre.getName() + "' has been added.");
            }
        }
    }

    @Override
    public void update(Genre genre) {
        boolean isSuccess = genreRepository.update(genre);

        if (!isSuccess) {
            throw new EntityNotFoundException(Genre.class,
                    "Genre with id = " + genre.getId() + " hasn't been found");
        }
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre findGenreByID(Integer genreID) {
        try {
            return genreRepository.findGenreByID(genreID);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(Genre.class, "Genre with id = " + genreID + " hasn't been found");
        }
    }

    @Override
    public Genre findByName(String genreName) {
        try {
            return genreRepository.findByName(genreName);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(Genre.class, "Genre with name '" + genreName + "' hasn't been found");
        }
    }

    @Override
    public boolean deleteById(Integer genreID) {
        return genreRepository.deleteById(genreID);
    }

    @Override
    public boolean deleteAll() {
        return genreRepository.deleteAll();
    }

}