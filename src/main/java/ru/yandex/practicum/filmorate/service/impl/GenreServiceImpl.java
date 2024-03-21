package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@AllArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private GenreRepository genreRepository;

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre findGenreByID(Integer genreID) {
        return genreRepository.findGenreByID(genreID);
    }

    @Override
    public Genre save(Genre newGenre) {
        return genreRepository.save(newGenre);
    }

    @Override
    public void saveMany(List<Genre> newGenres) {
        genreRepository.saveMany(newGenres);
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
