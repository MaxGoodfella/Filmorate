package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class GenreRepositoryImpl implements GenreRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(
                "select * from GENRES",
                genreRowMapper());

    }

    @Override
    public Genre findGenreByID(Integer genreID) {
        Genre genre = jdbcTemplate.queryForObject(
                "select * from GENRES where genre_id = ?",
                genreRowMapper(),
                genreID);

        return genre;
    }

//    @Override
//    public Genre save(Genre newGenre) {
//        String sqlQuery = "insert into GENRES(GENRE_NAME) " +
//                "values (?)";
//        jdbcTemplate.update(sqlQuery,
//                newGenre.getName());
//
//        return newGenre;
//        // работает, но надо подумать, как сделать так, чтобы он не пускал рейтинг с таким же именем - done with unique
//        // есть проблема - когда создаю новый рейтинг с тем же именем, ему дается айдишник всё равно,
//        // а так быть не должно!!! - надо фиксить
//
//        // и ещё, кстати, он в теле ответа не показывает айди, т.е. ему дают айди, но пишет null почему-то
//    }

//    @Override
//    public Genre save(Genre newGenre) {
//        String sqlQuery = "insert into GENRES(GENRE_NAME, EMAIL) " +
//                "values (?, ?)";
//        jdbcTemplate.update(sqlQuery,
//                newGenre.getName(),
//                newGenre.getEmail());
//
//        return newGenre;
//        // работает, но надо подумать, как сделать так, чтобы он не пускал рейтинг с таким же именем - done with unique
//        // есть проблема - когда создаю новый рейтинг с тем же именем, ему дается айдишник всё равно,
//        // а так быть не должно!!! - надо фиксить
//
//        // и ещё, кстати, он в теле ответа не показывает айди, т.е. ему дают айди, но пишет null почему-то
//    }

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
    public boolean deleteById(Integer genreID) {
        String sqlQuery = "delete from GENRES where genre_id = ?";

        return jdbcTemplate.update(sqlQuery, genreID) > 0;
    }

    @Override
    public boolean deleteAll() {
        String sqlQuery = "delete from GENRES";

        return jdbcTemplate.update(sqlQuery) > 0;
    }

    private static RowMapper<Genre> genreRowMapper() {
//        return (rs, rowNum) -> new Genre(
//                rs.getInt("genre_id"),
//                rs.getString("genre_name"));
        return (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("genre_name"));
    }

    private Map<String, Object> genreToMap(Genre genre) {
        return Map.of(
                "genre_name", genre.getName());
    }

}
