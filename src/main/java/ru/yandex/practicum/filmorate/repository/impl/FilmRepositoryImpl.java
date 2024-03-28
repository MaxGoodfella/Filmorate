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
import java.sql.Types;
import java.time.LocalDate;
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
                "INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID, POPULARITY) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Film film = newFilms.get(i);
                        ps.setString(1, film.getName());
                        ps.setString(2, film.getDescription());
                        ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                        ps.setInt(4, film.getDuration());
                        Integer ratingId = film.getRating() != null ? film.getRating() : null;
                        if (ratingId != null) {
                            ps.setInt(5, ratingId);
                        } else {
                            ps.setNull(5, Types.INTEGER);
                        }
                        ps.setInt(6, film.getPopularity());
                    }

                    @Override
                    public int getBatchSize() {
                        return newFilms.size();
                    }
                });
    }


    @Override
    public boolean update(Film film) {
        String sqlQuery = "update FILMS set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
                "RATING_ID = ?, POPULARITY = ? where FILM_ID = ?";
        
        int rowsAffected = jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getRating()
                , film.getPopularity()
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
        return jdbcTemplate.queryForObject(
                "select * from FILMS where NAME = ?",
                filmRowMapper(),
                filmName);
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
    public Film findByNameDescriptionReleaseDateAndDuration(String name, String description, LocalDate releaseDate, int duration) {
        List<Film> films = jdbcTemplate.query(
                    "SELECT * FROM FILMS WHERE NAME = ? AND DESCRIPTION = ? AND RELEASE_DATE = ? AND DURATION = ?",
                    filmRowMapper(),
                    name, description, releaseDate, duration);

        if (films.isEmpty()) {
            return null;
        } else {
            return films.get(0);
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


    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sqlQuery = "INSERT INTO FILM_FANS(FILM_ID, USER_ID) VALUES (?, ?)";

        jdbcTemplate.update(sqlQuery,
                filmId,
                userId);

        String sqlQuery2 = "UPDATE FILMS SET POPULARITY = " +
                "(SELECT COUNT(*) FROM FILM_FANS WHERE FILM_ID = ?) " +
                "WHERE FILM_ID = ?";

        jdbcTemplate.update(sqlQuery2, filmId, filmId);
    }

    @Override
    public boolean removeLike(Integer filmId, Integer userId) {
        String sqlQuery = "DELETE FROM FILM_FANS WHERE film_id = ? AND user_id = ?";

        int rowsDeleted = jdbcTemplate.update(sqlQuery, filmId, userId);

        if (rowsDeleted > 0) {
            String sqlQuery2 = "UPDATE FILMS SET POPULARITY = (SELECT COUNT(*) FROM FILM_FANS WHERE FILM_ID = ?) WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQuery2, filmId, filmId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Film> getTopByLikes(Integer count) {
        String sqlQuery = "SELECT * FROM FILMS ORDER BY popularity DESC LIMIT ?";

        return jdbcTemplate.query(sqlQuery, filmRowMapper(), count);
    }

    @Override
    public List<Integer> findFansIds(Integer filmId) {
        String sqlQuery = "SELECT user_id FROM FILM_FANS WHERE film_id = ? ORDER BY user_id";

        return jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId);
    }

    @Override
    public boolean existsLike(Integer filmId, Integer userId) {
        String sqlQuery = "SELECT COUNT(*) FROM FILM_FANS WHERE FILM_ID = ? AND USER_ID = ?";
        int count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId, userId);
        return count > 0;
    }


    @Override
    public void addGenres(Integer filmId, List<Integer> genreIds) {
        String sqlQuery = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Integer genreId = genreIds.get(i);

                ps.setInt(1, filmId);
                ps.setInt(2, genreId);
            }

            @Override
            public int getBatchSize() {
                return genreIds.size();
            }
        });
    }

    @Override
    public boolean removeGenre(Integer filmId, String genreName) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE film_id = ? AND genre_id = " +
                "(SELECT genre_id FROM GENRES WHERE genre_name = ?)";

        int rowsDeleted = jdbcTemplate.update(sqlQuery, filmId, genreName);

        return rowsDeleted > 0;
    }

    @Override
    public List<String> findGenresNames(Integer filmId) {
        String sqlQuery = "SELECT g.genre_name " +
                          "FROM GENRES AS g " +
                          "JOIN FILM_GENRE AS fg ON g.genre_id = fg.genre_id " +
                          "JOIN FILMS AS f ON f.film_id = fg.film_id " +
                          "WHERE fg.film_id = ?";

        return jdbcTemplate.queryForList(sqlQuery, String.class, filmId);
    }

    @Override
    public boolean existsGenre(Integer filmId, Integer genreId) {
        String sqlQuery = "SELECT COUNT(*) FROM FILM_GENRE WHERE film_id = ? AND genre_id = ?";
        int count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId, genreId);
        return count > 0;
    }



    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
         values.put("rating_id", film.getRating());
         values.put("popularity", film.getPopularity());

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