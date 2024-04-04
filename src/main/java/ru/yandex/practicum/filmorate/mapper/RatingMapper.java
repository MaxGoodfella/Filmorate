package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Component
public class RatingMapper implements RowMapper<Rating> {

    @Override
    public Rating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Rating(
                rs.getInt("rating_id"),
                rs.getString("rating_name"));
    }

    public Map<String, Object> toMap(Rating rating) {
        return Map.of(
                "rating_name", rating.getName());
    }

}