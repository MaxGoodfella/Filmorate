package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.RatingRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Repository
public class RatingRepositoryImpl implements RatingRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Rating save(Rating rating) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILM_RATING")
                .usingGeneratedKeyColumns("rating_id");

        int id = simpleJdbcInsert.executeAndReturnKey(ratingToMap(rating)).intValue();
        return rating.setId(id);
    }

    @Override
    public void saveMany(List<Rating> newRatings) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO FILM_RATING(RATING_NAME) VALUES (?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Rating rating = newRatings.get(i);
                        ps.setString(1, rating.getName());
                    }

                    @Override
                    public int getBatchSize() {
                        return newRatings.size();
                    }
                });
    }


    @Override
    public boolean update(Rating rating) {
        String sqlQuery = "UPDATE FILM_RATING SET RATING_NAME = ? WHERE RATING_ID = ?";
        int rowsAffected = jdbcTemplate.update(sqlQuery, rating.getName(), rating.getId());

        return rowsAffected > 0;
    }


    @Override
    public Rating findByID(Integer ratingID) {
        List<Rating> ratings = jdbcTemplate.query(
                "SELECT * FROM FILM_RATING WHERE RATING_ID = ? ORDER BY RATING_ID",
                ratingRowMapper(),
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
                ratingRowMapper(),
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
                ratingRowMapper(),
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
                ratingRowMapper());
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



    private static RowMapper<Rating> ratingRowMapper() {
        return (rs, rowNum) -> new Rating(
                rs.getInt("rating_id"),
                rs.getString("rating_name"));
    }

    private Map<String, Object> ratingToMap(Rating rating) {
        return Map.of(
                "rating_name", rating.getName());
    }

}