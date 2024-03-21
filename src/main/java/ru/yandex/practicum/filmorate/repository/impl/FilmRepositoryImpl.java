package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// не забыть про qualifier annotation

@Repository
@RequiredArgsConstructor
public class FilmRepositoryImpl implements FilmRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Film findById(Integer id) {

        return jdbcTemplate.queryForObject(
                "select * from FILMS where film_id = ?",
                filmRowMapper(),
                id);
    } // works

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(
                "select * from FILMS",
                filmRowMapper());
    } // works

//    public int save(Film film) {
//        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName("films")
//                .usingGeneratedKeyColumns("film_id");
//
//        return (int) simpleJdbcInsert.executeAndReturnKey(filmToMap(film));
//    }

    @Override
    public Film save(Film newFilm) {
        String sqlQuery = "insert into FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID, POPULARITY) " +
                "values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getRating(),
                newFilm.getLikes().size());

        return newFilm;
        // надо тестить
    }

    @Override
    public boolean deleteById(Integer filmID) {
        String sqlQuery = "delete from FILMS where film_id = ?";

        return jdbcTemplate.update(sqlQuery, filmID) > 0;
    } // works

    @Override
    public boolean deleteAll() {
        String sqlQuery = "delete from FILMS";

        return jdbcTemplate.update(sqlQuery) > 0;
    } // works



    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getRating());
        values.put("popularity", film.getLikes().size());

        return values;
    }


    private static RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> new Film(
                rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                rs.getInt("rating_id"),
                rs.getInt("popularity"));
    }
}
