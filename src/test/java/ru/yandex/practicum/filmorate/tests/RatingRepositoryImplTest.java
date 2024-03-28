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
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.impl.RatingRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingRepositoryImplTest {

    private final JdbcTemplate jdbcTemplate;

    private RatingRepositoryImpl ratingRepositoryImpl;

    @BeforeEach
    public void setUp() {
        ratingRepositoryImpl = new RatingRepositoryImpl(jdbcTemplate);
    }

    @AfterEach
    public void tearDown() {
        jdbcTemplate.execute("DELETE FROM FILM_RATING");
    }


    @Test
    public void testFindRatingByExistingId() {
        Rating newRating = new Rating(1, "PG13");
        Rating savedRating = ratingRepositoryImpl.save(newRating);

        assertDoesNotThrow(() -> ratingRepositoryImpl.findByID(savedRating.getId()));

        assertThat(savedRating)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newRating);
    }

    @Test
    public void testFindRatingByNotExistingId_shouldThrowEmptyResultDataAccessException() {
        Rating newRating = new Rating(1, "PG13");
        ratingRepositoryImpl.save(newRating);

        assertThrows(EmptyResultDataAccessException.class, () -> ratingRepositoryImpl.findByID(2));
    }

    @Test
    public void testFindRatingByExistingName() {
        Rating newRating = new Rating(1, "PG13");
        Rating savedRating = ratingRepositoryImpl.save(newRating);

        assertDoesNotThrow(() -> ratingRepositoryImpl.findByName(savedRating.getName()));

        assertThat(savedRating)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newRating);
    }

    @Test
    public void testFindRatingByNotExistingName_shouldReturnNull() {
        Rating newRating = new Rating(1, "PG13");
        ratingRepositoryImpl.save(newRating);

        assertNull(ratingRepositoryImpl.findByName("PG21"));
    }

    @Test
    public void testFindRatingIdByExistingName() {
        Rating newRating = new Rating(1, "PG13");
        Rating savedRating = ratingRepositoryImpl.save(newRating);

        assertDoesNotThrow(() -> ratingRepositoryImpl.findIdByName(savedRating.getName()));

        assertThat(savedRating)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newRating);
    }

    @Test
    public void testFindRatingIdByNotExistingName_shouldReturnNull() {
        Rating newRating = new Rating(1, "PG13");
        ratingRepositoryImpl.save(newRating);

        assertNull(ratingRepositoryImpl.findIdByName("PG21"));
    }



    @Test
    public void testFindAll() {
        Rating newRating1 = new Rating(1, "PG13");
        Rating newRating2 = new Rating(2, "PG17");
        List<Rating> newRatings = new ArrayList<>();
        newRatings.add(newRating1);
        newRatings.add(newRating2);

        ratingRepositoryImpl.save(newRating1);
        ratingRepositoryImpl.save(newRating2);

        List<Rating> savedRatings = ratingRepositoryImpl.findAll();

        assertThat(savedRatings)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newRatings);
    }


    @Test
    public void testSave() {
        Rating newRating = new Rating(1, "PG13");

        assertDoesNotThrow(() -> ratingRepositoryImpl.save(newRating));
    }

    @Test
    public void testSaveSameRating_shouldThrowDuplicateKeyException() {
        Rating newRating1 = new Rating(1, "PG13");
        Rating newRating2 = new Rating(2, "PG13");

        ratingRepositoryImpl.save(newRating1);

        assertThrows(DuplicateKeyException.class, () -> ratingRepositoryImpl.save(newRating2));
    }

    @Test
    public void testSaveMany() {
        Rating newRating1 = new Rating(1, "PG13");
        Rating newRating2 = new Rating(2, "PG17");
        List<Rating> newRatings = new ArrayList<>();
        newRatings.add(newRating1);
        newRatings.add(newRating2);

        assertDoesNotThrow(() -> ratingRepositoryImpl.saveMany(newRatings));
    }

    @Test
    public void testSaveMany_shouldThrowDuplicateKeyException() {
        Rating newRating1 = new Rating(1, "PG17");
        Rating newRating2 = new Rating(2, "PG17");
        List<Rating> newRatings = new ArrayList<>();
        newRatings.add(newRating1);
        newRatings.add(newRating2);

        assertThrows(DuplicateKeyException.class, () -> ratingRepositoryImpl.saveMany(newRatings));
    }


    @Test
    public void testDeleteByExistingId() {
        Rating newRating = new Rating(1, "PG13");

        Rating savedRating = ratingRepositoryImpl.save(newRating);

        assertDoesNotThrow(() -> ratingRepositoryImpl.deleteById(savedRating.getId()));
        assertThrows(EmptyResultDataAccessException.class, () -> ratingRepositoryImpl.findByID(1));
    }

    @Test
    public void testDeleteByNotExistingId() {
        Rating newRating = new Rating(1, "PG13");
        ratingRepositoryImpl.save(newRating);

        boolean deletionResult = ratingRepositoryImpl.deleteById(2);

        assertFalse(deletionResult);
    }

    @Test
    public void testDeleteAll() {
        Rating newRating1 = new Rating(1, "PG13");
        Rating newRating2 = new Rating(2, "PG17");
        Rating newRating3 = new Rating(3, "PG21");
        ratingRepositoryImpl.save(newRating1);
        ratingRepositoryImpl.save(newRating2);
        ratingRepositoryImpl.save(newRating3);

        assertDoesNotThrow(() -> ratingRepositoryImpl.deleteAll());
    }

}