package ru.yandex.practicum.filmorate.tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.mapper.RatingMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.impl.FilmRepositoryImpl;
import ru.yandex.practicum.filmorate.repository.impl.GenreRepositoryImpl;
import ru.yandex.practicum.filmorate.repository.impl.RatingRepositoryImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreRepositoryImplTest {

    private final JdbcTemplate jdbcTemplate;

    private static RatingRepositoryImpl ratingRepositoryImpl;

    private GenreRepositoryImpl genreRepositoryImpl;

    private static GenreMapper genreMapper;

    private static RatingMapper ratingMapper;

    private static FilmMapper filmMapper;

    @BeforeEach
    public void setUp() {
        genreMapper = new GenreMapper();
        ratingMapper = new RatingMapper();

        ratingRepositoryImpl = new RatingRepositoryImpl(jdbcTemplate, ratingMapper);
        genreRepositoryImpl = new GenreRepositoryImpl(jdbcTemplate, genreMapper);

        filmMapper = new FilmMapper(ratingRepositoryImpl, genreRepositoryImpl);


        jdbcTemplate.execute("DELETE FROM GENRES");
    }



    @Test
    public void testFindGenreByExistingId() {
        Genre newGenre = new Genre(1, "Comedy");
        Genre savedGenre = genreRepositoryImpl.save(newGenre);

        assertDoesNotThrow(() -> genreRepositoryImpl.findByID(savedGenre.getId()));

        assertThat(savedGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newGenre);
    }

    @Test
    public void testFindGenreByNotExistingId_shouldThrowEmptyResultDataAccessException() {
        Genre newGenre = new Genre(1, "Comedy");
        genreRepositoryImpl.save(newGenre);

        assertNull(genreRepositoryImpl.findByID(2));
    }

    @Test
    public void testFindGenreByExistingName() {
        Genre newGenre = new Genre(1, "Comedy");
        Genre savedGenre = genreRepositoryImpl.save(newGenre);

        assertDoesNotThrow(() -> genreRepositoryImpl.findByName(savedGenre.getName()));

        assertThat(savedGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newGenre);
    }

    @Test
    public void testFindGenreByNotExistingName_shouldReturnNull() {
        Genre newGenre = new Genre(1, "Comedy");
        genreRepositoryImpl.save(newGenre);

        assertNull(genreRepositoryImpl.findByName("Drama"));
    }

    @Test
    public void testFindGenreIdByExistingName() {
        Genre newGenre = new Genre(1, "Comedy");
        Genre savedGenre = genreRepositoryImpl.save(newGenre);

        assertDoesNotThrow(() -> genreRepositoryImpl.findIdByName(savedGenre.getName()));

        assertThat(savedGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newGenre);
    }

    @Test
    public void testFindGenreIdByNotExistingName_shouldReturnNull() {
        Genre newGenre = new Genre(1, "Comedy");
        genreRepositoryImpl.save(newGenre);

        assertNull(genreRepositoryImpl.findIdByName("Drama"));
    }

    @Test
    public void testFindAll() {
        Genre newGenre1 = new Genre(1, "Comedy");
        Genre newGenre2 = new Genre(2, "Drama");
        List<Genre> newGenres = new ArrayList<>();
        newGenres.add(newGenre1);
        newGenres.add(newGenre2);

        genreRepositoryImpl.save(newGenre1);
        genreRepositoryImpl.save(newGenre2);

        List<Genre> savedGenres = genreRepositoryImpl.findAll();

        assertThat(savedGenres)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newGenres);
    }


    @Test
    public void testSave() {
        Genre newGenre = new Genre(1, "Comedy");

        assertDoesNotThrow(() -> genreRepositoryImpl.save(newGenre));
    }

    @Test
    public void testSaveSameGenre_shouldThrowDuplicateKeyException() {
        Genre newGenre1 = new Genre(1, "Comedy");
        Genre newGenre2 = new Genre(2, "Comedy");

        genreRepositoryImpl.save(newGenre1);

        assertThrows(DuplicateKeyException.class, () -> genreRepositoryImpl.save(newGenre2));
    }

    @Test
    public void testDeleteByExistingId() {
        Genre newGenre = new Genre(1, "Comedy");

        Genre savedGenre = genreRepositoryImpl.save(newGenre);

        assertDoesNotThrow(() -> genreRepositoryImpl.deleteById(savedGenre.getId()));
        assertNull(genreRepositoryImpl.findByID(1));
    }

    @Test
    public void testDeleteByNotExistingId() {
        Genre newGenre = new Genre(1, "Comedy");
        genreRepositoryImpl.save(newGenre);

        boolean deletionResult = genreRepositoryImpl.deleteById(2);

        assertFalse(deletionResult);
    }

    @Test
    public void testDeleteAll() {
        Genre newGenre1 = new Genre(1, "Comedy");
        Genre newGenre2 = new Genre(2, "Drama");
        Genre newGenre3 = new Genre(3, "Thriller");
        genreRepositoryImpl.save(newGenre1);
        genreRepositoryImpl.save(newGenre2);
        genreRepositoryImpl.save(newGenre3);

        assertDoesNotThrow(() -> genreRepositoryImpl.deleteAll());
    }

    @Test
    public void testAdd() {

        RatingRepositoryImpl ratingRepositoryImpl = new RatingRepositoryImpl(jdbcTemplate, ratingMapper);

        Rating newRating1 = new Rating(1, "PG13");
        Rating newRating2 = new Rating(2, "PG17");
        Rating savedRating1 = ratingRepositoryImpl.save(newRating1);
        Rating savedRating2 = ratingRepositoryImpl.save(newRating2);

        Genre newGenre1 = new Genre(1, "Comedy");
        Genre newGenre2 = new Genre(2, "Drama");
        Genre newGenre3 = new Genre(3, "Thriller");

        Genre savedGenre1 = genreRepositoryImpl.save(newGenre1);
        Genre savedGenre2 = genreRepositoryImpl.save(newGenre2);
        Genre savedGenre3 = genreRepositoryImpl.save(newGenre3);

        List<Genre> film1Genres = new ArrayList<>();
        film1Genres.add(savedGenre1);
        film1Genres.add(savedGenre2);
        film1Genres.add(savedGenre3);

        List<Genre> film2Genres = new ArrayList<>();
        film2Genres.add(savedGenre1);
        film2Genres.add(savedGenre2);


        FilmRepositoryImpl filmRepositoryImpl = new FilmRepositoryImpl(jdbcTemplate,
                filmMapper);

        Film newFilm1 = new Film("Name1", "Description2",
                LocalDate.of(1990, 12, 12), 100, savedRating1, 0);
        Film newFilm2 = new Film("Name2", "Description2",
                LocalDate.of(1990, 12, 12), 100, savedRating2, 0);

        Film savedFilm1 = filmRepositoryImpl.save(newFilm1);
        Film savedFilm2 = filmRepositoryImpl.save(newFilm2);


        genreRepositoryImpl.add(savedFilm1.getId(), film1Genres);
        genreRepositoryImpl.add(savedFilm2.getId(), film2Genres);

        assertEquals(3, genreRepositoryImpl.findGenresForFilm(savedFilm1.getId()).size());
        assertTrue(genreRepositoryImpl.findGenresForFilm(savedFilm1.getId()).contains(savedGenre1));
        assertTrue(genreRepositoryImpl.findGenresForFilm(savedFilm1.getId()).contains(savedGenre2));
        assertTrue(genreRepositoryImpl.findGenresForFilm(savedFilm1.getId()).contains(savedGenre3));
        assertEquals(2, genreRepositoryImpl.findGenresForFilm(savedFilm2.getId()).size());
        assertTrue(genreRepositoryImpl.findGenresForFilm(savedFilm2.getId()).contains(savedGenre1));
        assertTrue(genreRepositoryImpl.findGenresForFilm(savedFilm2.getId()).contains(savedGenre2));

    }

}