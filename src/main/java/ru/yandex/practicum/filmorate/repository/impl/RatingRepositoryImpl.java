package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.RatingMapper;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.RatingRepository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class RatingRepositoryImpl implements RatingRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RatingMapper ratingMapper;


    @Override
    public Rating save(Rating rating) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILM_RATING")
                .usingGeneratedKeyColumns("rating_id");

        int id = simpleJdbcInsert.executeAndReturnKey(ratingMapper.toMap(rating)).intValue();
        return rating.setId(id);
    }


    @Override
    public boolean update(Rating rating) {
        String sqlQuery = "UPDATE FILM_RATING SET RATING_NAME = ? WHERE RATING_ID = ?";
        int rowsAffected = jdbcTemplate.update(sqlQuery,
                rating.getName(),
                rating.getId());

        return rowsAffected > 0;
    }


    @Override
    public Rating findByID(Integer ratingID) {
        List<Rating> ratings = jdbcTemplate.query(
                "SELECT * FROM FILM_RATING WHERE RATING_ID = ? ORDER BY RATING_ID",
                ratingMapper,
                ratingID);

        if (ratings.isEmpty()) {
            return null;
        } else {
            return ratings.get(0);
        }
    }

    @Override
    public Rating findByFilmId(Integer filmId) {

        String sqlQuery = "SELECT * FROM FILM_RATING AS FR " +
                "JOIN FILMS AS F ON F.rating_id = FR.RATING_ID " +
                "WHERE F.FILM_ID = ?";

        List<Rating> ratings = jdbcTemplate.query(
                sqlQuery,
                ratingMapper,
                filmId);

        if (ratings.isEmpty()) {
            return null;
        } else {
            return ratings.get(0);
        }
    }

    @Override
    public Rating findByName(String ratingName) {
        List<Rating> ratings = jdbcTemplate.query(
                "SELECT * FROM FILM_RATING WHERE RATING_NAME = ?",
                ratingMapper,
                ratingName
        );

        if (ratings.isEmpty()) {
            return null;
        } else {
            return ratings.get(0);
        }
    }

    @Override
    public Integer findIdByName(String name) {
        String sql = "SELECT RATING_ID FROM FILM_RATING WHERE RATING_NAME = ?";
        List<Integer> ratingIds = jdbcTemplate.queryForList(sql, Integer.class, name);

        if (!ratingIds.isEmpty()) {
            return ratingIds.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Rating> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM FILM_RATING ORDER BY RATING_ID",
                ratingMapper);
    }


    @Override
    public boolean deleteById(Integer ratingID) {
        String sqlQuery = "DELETE FROM FILM_RATING WHERE RATING_ID = ?";

        return jdbcTemplate.update(sqlQuery, ratingID) > 0;
    }

    @Override
    public boolean deleteAll() {
        String sqlQuery = "DELETE FROM FILM_RATING";

        return jdbcTemplate.update(sqlQuery) > 0;
    }

}