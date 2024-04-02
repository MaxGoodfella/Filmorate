package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.RatingRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;


@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmRepositoryImpl implements FilmRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RatingRepository ratingRepository;

    private final GenreRepository genreRepository;


//    @Override
//    public Film save(Film film) {
//        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName("FILMS")
//                .usingGeneratedKeyColumns("film_id");
//
//        Map<String, Object> parameters = filmToMap(film);
//        Number newFilmId = simpleJdbcInsert.executeAndReturnKey(parameters);
//
//        film.setId(newFilmId.intValue());
//
//        return film;
//    }





    //        List<Genre> genres = film.getGenres();
//
//        for (Genre genre : genres) {
//            if (genreRepository.findByID(genre.getId()) == null) {
//                throw new IllegalArgumentException("All genres or one of genres " + film.getGenres() + " do not exist");
//            }
//        }
//
//        List<Genre> filmGenres = genreRepository.add(film.getId(), genres);
//        film.setGenres(filmGenres);
//
//        return film;

//        List<Genre> genres = film.getGenres();
//        if (genres != null) {
//            for (Genre genre : genres) {
//                if (genreRepository.findByID(genre.getId()) == null) {
//                    throw new IllegalArgumentException("All genres or one of genres " + film.getGenres() + " do not exist");
//                }
//            }
//
//            List<Genre> filmGenres = genreRepository.add(film.getId(), genres);
//            film.setGenres(filmGenres);
//        }
//
//        return film;


    @Override
    public Film save(Film film) {

        Rating existingRating = ratingRepository.findByID(film.getMpa().getId());
        if (existingRating == null) {
            throw new IllegalArgumentException("Rating with id " + film.getMpa().getId() + " does not exist");
        }

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("film_id");

        Map<String, Object> parameters = filmToMap(film);
        Number newFilmId = simpleJdbcInsert.executeAndReturnKey(parameters);
        film.setId(newFilmId.intValue());


        List<Genre> genres = film.getGenres();
        if (genres != null) {
            Set<Integer> genreIds = new HashSet<>();
            List<Genre> uniqueGenres = new ArrayList<>();

            for (Genre genre : genres) {
                if (!genreIds.contains(genre.getId())) {
                    genreIds.add(genre.getId());
                    uniqueGenres.add(genre);
                }
            }

            List<Genre> filmGenres = new ArrayList<>();
            for (Genre uniqueGenre : uniqueGenres) {
                Genre existingGenre = genreRepository.findByID(uniqueGenre.getId());
                if (existingGenre == null) {
                    throw new IllegalArgumentException("Genre with id " + uniqueGenre.getId() + " does not exist");
                }
                filmGenres.add(existingGenre);
            }

            genreRepository.add(film.getId(), filmGenres);
            film.setGenres(filmGenres);
        }

        return film;

    }

//    @Override
//    public void saveMany(List<Film> newFilms) {
//        jdbcTemplate.batchUpdate(
//                "INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID, POPULARITY) " +
//                        "VALUES (?, ?, ?, ?, ?, ?)",
//                new BatchPreparedStatementSetter() {
//                    @Override
//                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        Film film = newFilms.get(i);
//                        ps.setString(1, film.getName());
//                        ps.setString(2, film.getDescription());
//                        ps.setDate(3, Date.valueOf(film.getReleaseDate()));
//                        ps.setInt(4, film.getDuration());
//                        Integer ratingId = film.getMpa() != null ? film.getMpa().getId() : null;
//                        if (ratingId != null) {
//                            ps.setInt(5, ratingId);
//                        } else {
//                            ps.setNull(5, Types.INTEGER);
//                        }
//                        ps.setInt(6, film.getPopularity());
//                    }
//
//                    @Override
//                    public int getBatchSize() {
//                        return newFilms.size();
//                    }
//                });
//    }

//    @Override
//    public void saveMany(List<Film> newFilms) {
//        jdbcTemplate.batchUpdate(
//                "INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
//                        "VALUES (?, ?, ?, ?, ?)",
//                new BatchPreparedStatementSetter() {
//                    @Override
//                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        Film film = newFilms.get(i);
//                        ps.setString(1, film.getName());
//                        ps.setString(2, film.getDescription());
//                        ps.setDate(3, Date.valueOf(film.getReleaseDate()));
//                        ps.setInt(4, film.getDuration());
//                        Integer ratingId = film.getMpa() != null ? film.getMpa().getId() : null;
//                        if (ratingId != null) {
//                            ps.setInt(5, ratingId);
//                        } else {
//                            ps.setNull(5, Types.INTEGER);
//                        }
//                    }
//
//                    @Override
//                    public int getBatchSize() {
//                        return newFilms.size();
//                    }
//                });
//    }


//    @Override
//    public boolean update(Film film) {
//        String sqlQuery = "update FILMS set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
//                "RATING_ID = ?, POPULARITY = ? where FILM_ID = ?";
//
//        int rowsAffected = jdbcTemplate.update(sqlQuery
//                , film.getName()
//                , film.getDescription()
//                , film.getReleaseDate()
//                , film.getDuration()
//                , film.getMpa()
//                , film.getPopularity()
//                , film.getId());
//
//        return rowsAffected > 0;
//    }

    @Override
    public boolean update(Film film) {
        String sqlQuery = "update FILMS set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
                "RATING_ID = ? where FILM_ID = ?";

        int rowsAffected = jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
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

        // return rowsDeleted > 0;

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

//    @Override
//    public List<Film> getTopByLikes(Integer count) {
//        //String sqlQuery = "SELECT * FROM FILMS ORDER BY popularity DESC LIMIT ?";
//
//        String sqlQuery = "SELECT f.* " +
//                          "FROM FILMS AS f " +
//                          "JOIN FILM_FANS AS ff ON f.FILM_ID = ff.FILM_ID " +
//                          "GROUP BY ff.FILM_ID " +
//                          "ORDER BY COUNT(ff.USER_ID) DESC " +
//                          "LIMIT ?";
//
//        return jdbcTemplate.query(sqlQuery, filmRowMapper(), count);
//    }

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

//// мощно рефакторить жанры!
//
//
////    @Override
////    public void addGenres(Integer filmId, List<Integer> genreIds) {
////        String sqlQuery = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
////
////        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
////            @Override
////            public void setValues(PreparedStatement ps, int i) throws SQLException {
////                Integer genreId = genreIds.get(i);
////
////                ps.setInt(1, filmId);
////                ps.setInt(2, genreId);
////            }
////
////            @Override
////            public int getBatchSize() {
////                return genreIds.size();
////            }
////        });
////    }
//
//    @Override
//    public List<Genre> addGenres(Integer filmId, List<Genre> genres) {
//        String sqlQuery = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
//
//        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                //Integer genreId = genreIds.get(i);
//
//                ps.setInt(1, filmId);
//                ps.setInt(2, genres.get(i).getId());
//            }
//
//            @Override
//            public int getBatchSize() {
//                return genres.size();
//            }
//        });
//
//        return findGenresForFilm(filmId);
//    }
//
////    @Override
////    public boolean removeGenre(Integer filmId, String genreName) {
////        String sqlQuery = "DELETE FROM FILM_GENRE WHERE film_id = ? AND genre_id = " +
////                "(SELECT genre_id FROM GENRES WHERE genre_name = ?)";
////
////        int rowsDeleted = jdbcTemplate.update(sqlQuery, filmId, genreName);
////
////        return rowsDeleted > 0;
////    }
//
//    @Override
//    public boolean removeGenre(Integer filmId, Integer genreId) {
//        String sqlQuery = "DELETE FROM FILM_GENRE WHERE film_id = ? AND genre_id = ?";
//
//        int rowsDeleted = jdbcTemplate.update(sqlQuery, filmId, genreId);
//
//        return rowsDeleted > 0;
//    }
//
////    @Override
////    public List<String> findGenresNames(Integer filmId) {
////        String sqlQuery = "SELECT g.genre_name " +
////                          "FROM GENRES AS g " +
////                          "JOIN FILM_GENRE AS fg ON g.genre_id = fg.genre_id " +
////                          "JOIN FILMS AS f ON f.film_id = fg.film_id " +
////                          "WHERE fg.film_id = ?";
////
////        return jdbcTemplate.queryForList(sqlQuery, String.class, filmId);
////    }
//
////    @Override
////    public List<Genre> findGenresForFilm(Integer filmId) {
////        String sqlQuery = "SELECT * " +
////                "FROM GENRES AS g " +
////                "JOIN FILM_GENRE AS fg ON g.genre_id = fg.genre_id " +
////                "JOIN FILMS AS f ON f.film_id = fg.film_id " +
////                "WHERE fg.film_id = ?";
////
////        return jdbcTemplate.queryForList(sqlQuery, Genre.class, filmId);
////    }
//
////    @Override
////    public List<Genre> findGenresForFilm(Integer filmId) {
////        String sqlQuery = "SELECT g.* " +
////                "FROM GENRES AS g " +
////                "JOIN FILM_GENRE AS fg ON g.genre_id = fg.genre_id " +
////                "JOIN FILMS AS f ON f.film_id = fg.film_id " +
////                "WHERE fg.film_id = ?";
////
////        return jdbcTemplate.queryForList(sqlQuery, Genre.class, filmId);
////    }
//
//    public List<Genre> findGenresForFilm(Integer filmId) {
//        String sqlQuery = "select distinct G.GENRE_ID, G.GENRE_NAME from FILM_GENRE AS FG" +
//                "    left join GENRES AS G ON FG.GENRE_ID = G.GENRE_ID\n" +
//                "    where FILM_ID = ?";
//        return jdbcTemplate.query(sqlQuery, this::mapRow, filmId);
//    }
//
//    @Override
//    public List<Integer> findGenresIdsForFilm(Integer filmId) {
//        String sqlQuery = "SELECT g.genre_id " +
//                "FROM GENRES AS g " +
//                "JOIN FILM_GENRE AS fg ON g.genre_id = fg.genre_id " +
//                "JOIN FILMS AS f ON f.film_id = fg.film_id " +
//                "WHERE fg.film_id = ?";
//
//        return jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId);
//    }
//
//    @Override
//    public boolean existsGenre(Integer filmId, Integer genreId) {
//        String sqlQuery = "SELECT COUNT(*) FROM FILM_GENRE WHERE film_id = ? AND genre_id = ?";
//        int count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId, genreId);
//        return count > 0;
//    }


//    private Map<String, Object> filmToMap(Film film) {
//        Map<String, Object> values = new HashMap<>();
//        values.put("name", film.getName());
//        values.put("description", film.getDescription());
//        values.put("release_date", film.getReleaseDate());
//        values.put("duration", film.getDuration());
//        Integer ratingId = film.getMpa() != null ? film.getMpa().getId() : null;
//        values.put("rating_id", ratingId);
//        //values.put("popularity", film.getPopularity());
//
//        return values;
//    }


    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getMpa().getId());
        values.put("popularity", film.getPopularity());

        return values;
    }

    private RowMapper<Film> filmRowMapper() {

        return (rs, rowNum) -> new Film(
                rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                ratingRepository.findByID(rs.getInt("rating_id")),
                rs.getInt("popularity"),
                genreRepository.findGenresForFilm(rs.getInt("film_id")));
    }

}