package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
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
    public List<Rating> findAll() {
        return jdbcTemplate.query(
                "select * from FILM_RATING",
                ratingRowMapper());
    }

    @Override
    public Rating findRatingByID(Integer ratingID) {
        Rating rating = jdbcTemplate.queryForObject(
                "select * from FILM_RATING where rating_id = ?",
                ratingRowMapper(),
                ratingID);

        return rating;
    }

//    @Override
//    public Rating save(Rating newRating) {
//        String sqlQuery = "insert into FILM_RATING(RATING_NAME) " +
//                "values (?)";
//        jdbcTemplate.update(sqlQuery,
//                newRating.getName());
//
//        return newRating;
//    }
        // работает, но надо подумать, как сделать так, чтобы он не пускал рейтинг с таким же именем - done with unique
        // есть проблема - когда создаю новый рейтинг с тем же именем, ему дается айдишник всё равно,
        // а так быть не должно!!! - надо фиксить

        // и ещё, кстати, он в теле ответа не показывает айди, т.е. ему дают айди, но пишет null почему-то
    // }

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
                "insert into FILM_RATING(RATING_NAME) values (?)",
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
    public boolean deleteById(Integer ratingID) {
        String sqlQuery = "delete from FILM_RATING where rating_id = ?";

        return jdbcTemplate.update(sqlQuery, ratingID) > 0;
    }

    @Override
    public boolean deleteAll() {
        String sqlQuery = "delete from FILM_RATING";

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
