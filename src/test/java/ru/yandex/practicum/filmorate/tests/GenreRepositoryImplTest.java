package ru.yandex.practicum.filmorate.tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.impl.GenreRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreRepositoryImplTest {

    private final JdbcTemplate jdbcTemplate;

    private GenreRepositoryImpl genreRepositoryImpl;

    @BeforeEach
    public void setUp() {
        genreRepositoryImpl = new GenreRepositoryImpl(jdbcTemplate);
    }

//    @AfterEach
//    public void tearDown() {
//        jdbcTemplate.execute("DELETE FROM GENRES");
//    }


    @Test
    public void testFindGenreByExistingId() {
        Genre newGenre = new Genre(1, "Comedy");
        Genre savedGenre = genreRepositoryImpl.save(newGenre);

        assertDoesNotThrow(() -> genreRepositoryImpl.findGenreByID(savedGenre.getId()));

        assertThat(savedGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newGenre);
    }

    @Test
    public void testFindGenreByNotExistingId_shouldThrowEmptyResultDataAccessException() {
        Genre newGenre = new Genre(1, "Comedy");
        genreRepositoryImpl.save(newGenre);

        assertThrows(EmptyResultDataAccessException.class, () -> genreRepositoryImpl.findGenreByID(2));
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
    public void testSaveMany() {
        Genre newGenre1 = new Genre(1, "Comedy");
        Genre newGenre2 = new Genre(2, "Drama");
        List<Genre> newGenres = new ArrayList<>();
        newGenres.add(newGenre1);
        newGenres.add(newGenre2);

        assertDoesNotThrow(() -> genreRepositoryImpl.saveMany(newGenres));
    }

    @Test
    public void testSaveMany_shouldThrowDuplicateKeyException() {
        Genre newGenre1 = new Genre(1, "Comedy");
        Genre newGenre2 = new Genre(2, "Comedy");
        List<Genre> newGenres = new ArrayList<>();
        newGenres.add(newGenre1);
        newGenres.add(newGenre2);

        assertThrows(DuplicateKeyException.class, () -> genreRepositoryImpl.saveMany(newGenres));
    }


    @Test
    public void testDeleteByExistingId() {
        Genre newGenre = new Genre(1, "Comedy");

        Genre savedGenre = genreRepositoryImpl.save(newGenre);

        assertDoesNotThrow(() -> genreRepositoryImpl.deleteById(savedGenre.getId()));
        assertThrows(EmptyResultDataAccessException.class, () -> genreRepositoryImpl.findGenreByID(1));
    }

    @Test
    public void testDeleteByNotExistingId_shouldThrowEmptyResultDataAccessException() {
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

}