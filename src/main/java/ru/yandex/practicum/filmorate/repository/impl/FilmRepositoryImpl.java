package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmRepositoryImpl implements FilmRepository {

    private final JdbcTemplate jdbcTemplate;

    private final FilmMapper filmMapper;


    @Override
    public Film save(Film film) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("film_id");

        Map<String, Object> parameters = filmMapper.toMap(film);
        Number newFilmId = simpleJdbcInsert.executeAndReturnKey(parameters);
        film.setId(newFilmId.intValue());

        return film;

    }


    @Override
    public boolean update(Film film) {
        String sqlQuery = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
                "RATING_ID = ?, POPULARITY = ? WHERE FILM_ID = ?";

        int rowsAffected = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getPopularity(),
                film.getId());

        return rowsAffected > 0;
    }


    @Override
    public Film findById(Integer id) {
        String sqlQuery = "SELECT f.*, fr.RATING_NAME " +
                "FROM FILMS f " +
                "JOIN FILM_RATING fr ON f.RATING_ID = fr.RATING_ID " +
                "WHERE f.FILM_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, filmMapper, id);
    }

    @Override
    public Film findByName(String filmName) {
        return jdbcTemplate.queryForObject(
                "SELECT f.*, fr.RATING_NAME " +
                        "FROM FILMS f " +
                        "JOIN FILM_RATING fr ON f.RATING_ID = fr.RATING_ID " +
                        "WHERE f.NAME = ?",
                filmMapper,
                filmName);
    }

    @Override
    public Integer findIdByName(String name) {
        String sql = "SELECT FILM_ID FROM FILMS WHERE NAME = ?";
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
                "SELECT f.*, fr.RATING_NAME " +
                        "FROM FILMS f " +
                        "JOIN FILM_RATING fr ON f.RATING_ID = fr.RATING_ID " +
                        "WHERE f.NAME = ? AND f.DESCRIPTION = ? AND f.RELEASE_DATE = ? AND f.DURATION = ?",
                filmMapper,
                name, description, releaseDate, duration);

        if (films.isEmpty()) {
            return null;
        } else {
            return films.get(0);
        }
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT f.*, fr.RATING_NAME " +
                "FROM FILMS f " +
                "JOIN FILM_RATING fr ON f.RATING_ID = fr.RATING_ID";
        return jdbcTemplate.query(sqlQuery, filmMapper);
    }


    @Override
    public boolean deleteById(Integer filmID) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";

        return jdbcTemplate.update(sqlQuery, filmID) > 0;
    }

    @Override
    public boolean deleteAll() {
        String sqlQuery = "DELETE FROM FILMS";

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
        String sqlQuery = "DELETE FROM FILM_FANS WHERE FILM_ID = ? AND USER_ID = ?";

        int rowsDeleted = jdbcTemplate.update(sqlQuery, filmId, userId);

        if (rowsDeleted > 0) {
            String sqlQuery2 = "UPDATE FILMS SET POPULARITY = " +
                    "(SELECT COUNT(*) FROM FILM_FANS WHERE FILM_ID = ?) " +
                    "WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQuery2, filmId, filmId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Film> getTopByLikes(Integer count) {
        String sqlQuery = "SELECT f.*, fr.RATING_NAME " +
                "FROM FILMS f JOIN FILM_RATING fr ON f.RATING_ID = fr.RATING_ID " +
                "ORDER BY POPULARITY DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, filmMapper, count);
    }

    @Override
    public List<Integer> findFansIds(Integer filmId) {
        String sqlQuery = "SELECT USER_ID FROM FILM_FANS WHERE FILM_ID = ? ORDER BY USER_ID";

        return jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId);
    }

    @Override
    public boolean existsLike(Integer filmId, Integer userId) {
        String sqlQuery = "SELECT COUNT(*) FROM FILM_FANS WHERE FILM_ID = ? AND USER_ID = ?";
        int count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId, userId);
        return count > 0;
    }

}