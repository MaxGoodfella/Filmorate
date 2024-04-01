package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class GenreRepositoryImpl implements GenreRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Genre save(Genre genre) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("GENRES")
                .usingGeneratedKeyColumns("genre_id");

        int id = simpleJdbcInsert.executeAndReturnKey(genreToMap(genre)).intValue();
        return genre.setId(id);
    }

    @Override
    public void saveMany(List<Genre> newGenres) {
        jdbcTemplate.batchUpdate(
                "insert into GENRES(GENRE_NAME) values (?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Genre genre = newGenres.get(i);
                        ps.setString(1, genre.getName());
                    }

                    @Override
                    public int getBatchSize() {
                        return newGenres.size();
                    }
                });
    }


    @Override
    public boolean update(Genre genre) {
        String sqlQuery = "UPDATE GENRES SET GENRE_NAME = ? WHERE GENRE_ID = ?";
        int rowsAffected = jdbcTemplate.update(sqlQuery, genre.getName(), genre.getId());

        return rowsAffected > 0;
    }


    @Override
    public Genre findByID(Integer genreID) {
        List<Genre> genres = jdbcTemplate.query(
                "select * from GENRES where genre_id = ? order by genre_id",
                genreRowMapper(),
                genreID);

        if (genres.isEmpty()) {
            return null;
        } else {
            return genres.get(0);
        }
    }

    @Override
    public Genre findByName(String genreName) {
        List<Genre> genres = jdbcTemplate.query(
                "SELECT * FROM GENRES WHERE GENRE_NAME = ?",
                genreRowMapper(),
                genreName
        );

        if (genres.isEmpty()) {
            return null;
        } else {
            return genres.get(0);
        }
    }

    @Override
    public Integer findIdByName(String name) {
        String sql = "SELECT genre_id FROM genres WHERE genre_name = ?";
        List<Integer> genreIds = jdbcTemplate.queryForList(sql, Integer.class, name);

        if (!genreIds.isEmpty()) {
            return genreIds.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(
                "select * from GENRES ORDER BY GENRE_ID",
                genreRowMapper());
    }


    @Override
    public boolean deleteById(Integer genreID) {
        String sqlQuery = "delete from GENRES where genre_id = ?";

        return jdbcTemplate.update(sqlQuery, genreID) > 0;
    }

    @Override
    public boolean deleteAll() {
        String sqlQuery = "delete from GENRES";

        return jdbcTemplate.update(sqlQuery) > 0;
    }


    @Override
    public List<Genre> add(Integer filmId, List<Genre> genres) {
        String sqlQuery = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {

                ps.setInt(1, filmId);
                ps.setInt(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });

        return findGenresForFilm(filmId);
    }

    @Override
    public List<Genre> findGenresForFilm(Integer filmId) {
        String sqlQuery = "select distinct G.GENRE_ID, G.GENRE_NAME from FILM_GENRE AS FG " +
                "left join GENRES AS G ON FG.GENRE_ID = G.GENRE_ID " +
                "where FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, genreRowMapper(), filmId);
    }



    private static RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("genre_name"));
    }

    private Map<String, Object> genreToMap(Genre genre) {
        return Map.of(
                "genre_name", genre.getName());
    }

}