package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.RatingRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FilmMapper implements RowMapper<Film> {

    //private final RatingRepository ratingRepository;

    // private final GenreRepository genreRepository;


    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
//        return new Film(
//                rs.getInt("film_id"),
//                rs.getString("name"),
//                rs.getString("description"),
//                rs.getDate("release_date").toLocalDate(),
//                rs.getInt("duration"),
//                ratingRepository.findByID(rs.getInt("rating_id")),
//                rs.getInt("popularity"),
//                //genreRepository.findGenresForFilm(rs.getInt("film_id")));

        int filmId = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        // int ratingId = rs.getInt("rating_id");
        int popularity = rs.getInt("popularity");
        //Rating rating = new Rating(rs.getInt("rating_id"), rs.getString("rating_name"));
        Rating rating = new Rating(rs.getInt("rating_id"), rs.getString("rating_name"));
        //Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));

        Film film = new Film(filmId, name, description, releaseDate, duration, rating, popularity);
        //film.addGenre(genre);
        return film;
    }

    public Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getMpa().getId());

        return values;
    }

}