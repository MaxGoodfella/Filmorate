package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class FilmRepositoryImpl implements FilmRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Film save(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("film_id");

        Map<String, Object> parameters = filmToMap(film);
        Number newFilmId = simpleJdbcInsert.executeAndReturnKey(parameters);

        film.setId(newFilmId.intValue());

        return film;
    }

    @Override
    public void saveMany(List<Film> newFilms) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION) VALUES (?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Film film = newFilms.get(i);
                        ps.setString(1, film.getName());
                        ps.setString(2, film.getDescription());
                        ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                        ps.setInt(4, film.getDuration());
                    }

                    @Override
                    public int getBatchSize() {
                        return newFilms.size();
                    }
                });
    }

    @Override
    public boolean update(Film film) {
        String sqlQuery = "update FILMS set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ? " +
                "where FILM_ID = ?";
        
        int rowsAffected = jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getId());

        return rowsAffected > 0;
    }

    @Override
    public Film findById(Integer id) {
        return jdbcTemplate.queryForObject(
                "select * from FILMS where film_id = ?",
                filmRowMapper(),
                id);
    }

    @Override
    public Film findByName(String filmName) {
        Film film = jdbcTemplate.queryForObject(
                "select * from FILMS where NAME = ?",
                filmRowMapper(),
                filmName);

        return film;
    }

    @Override
    public Integer findIdByName(String name) {
        String sql = "SELECT film_id FROM films WHERE name = ?";
        List<Integer> filmIds = jdbcTemplate.queryForList(sql, Integer.class, name);
        if (!filmIds.isEmpty()) {
            return filmIds.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(
                "select * from FILMS",
                filmRowMapper());
    }


    @Override
    public boolean deleteById(Integer filmID) {
        String sqlQuery = "delete from FILMS where film_id = ?";

        return jdbcTemplate.update(sqlQuery, filmID) > 0;
    }

    @Override
    public boolean deleteAll() {
        String sqlQuery = "delete from FILMS";

        return jdbcTemplate.update(sqlQuery) > 0;
    }



    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        // values.put("rating_id", film.getRating());
        // values.put("popularity", film.getLikes().size());

        return values;
    }

    private static RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> new Film(
                rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"));

        //    ,
//            rs.getInt("rating_id"),
//            rs.getInt("popularity")

    }


}