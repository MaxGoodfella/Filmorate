package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private int generatedID = 0;


    @Override
    public Film create(Film film) {
        for (Film existingFilm : films.values()) {
            if (existingFilm.getName().equals(film.getName()) &&
                    existingFilm.getDescription().equals(film.getDescription()) &&
                    existingFilm.getDuration() == film.getDuration() &&
                    existingFilm.getReleaseDate().equals(film.getReleaseDate())) {
                log.warn("Регистрация фильма не удалась. Фильм с названием {}, описанием {}, продолжительностью {} " +
                                "и датой выхода {} уже существует.",
                        film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate());
                throw new EntityAlreadyExistsException(Film.class, "Фильм с названием " + film.getName() + " уже зарегистрирован.");
            }
        }

        film.setId(generateID());

        films.put(film.getId(), film);
        log.info("Фильм успешно зарегистрирован. Название: {}", film.getName());
        return film;
    }

    @Override
    public Film put(Film updatedFilm) {
        int idToUpdate = updatedFilm.getId();

        for (Film film : films.values()) {
            if (film.getId() == idToUpdate) {
                film.setDescription(updatedFilm.getDescription());
                film.setName(updatedFilm.getName());
                film.setReleaseDate(updatedFilm.getReleaseDate());
                film.setDuration(updatedFilm.getDuration());

                log.info("Фильм c id {} успешно обновлен. Название: {}", idToUpdate, updatedFilm.getName());
                return film;
            }
        }
        log.warn("Фильм c id {} для обновления не найден.", idToUpdate);
        throw new EntityNotFoundException(Film.class, "Фильм с id " + idToUpdate + " не найден.");
    }

    @Override
    public Map<Integer, Film> findAll() {
        return films;
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        Film film = findFilmByID(filmId);

        if (film == null) {
            log.warn("Фильм с ID {} не найден.", filmId);
            throw new EntityNotFoundException(Film.class, "Фильм с ID " + filmId + " не найден.");
        }

        Set<Long> listOfFans = film.getLikes();
        long idToAdd = userId;

        if (listOfFans.contains(idToAdd)) {
            log.warn("Пользователь с ID {} уже добавил фильм с названием {} в понравившиеся.",
                    userId, film.getName());
            throw new EntityAlreadyExistsException(User.class, "Пользователь с ID " +
                    userId + " уже добавил в список понравившихся фильм с названием " +
                    film.getName() + ".");
        }

        listOfFans.add(idToAdd);
        log.info("Фильм с названием {} успешно добавлен в список понравившихся фильмов пользователя с ID {}.",
                film.getName(), userId);
        return film;
    }

    @Override
    public Film removeLike(Integer filmId, Integer userId) {
        Film film = findFilmByID(filmId);
        if (film == null) {
            log.warn("Фильм с ID {} не найден.", filmId);
            throw new EntityNotFoundException(Film.class, "Фильм с ID " + filmId + " не найден.");
        }

        Set<Long> listOfFans = film.getLikes();
        long idToRemove = userId;

        if (!listOfFans.contains(idToRemove)) {
            log.warn("Фильм с названием {} не найден в списке понравившихся фильмов пользователя с ID {}.",
                    film.getName(), userId);
            throw new EntityNotFoundException(User.class, "Фильм с названием " + film.getName() +
                    " не найден в списке понравившихся фильмов пользователя с ID " + userId + ".");
        }

        listOfFans.remove(idToRemove);
        log.info("Фильм с названием {} успешно удалён из списка понравившихся фильмов пользователя с ID {}.",
                film.getName(), userId);
        return film;
    }


    @Override
    public List<Film> getTopByLikes(Integer count) {
        Map<Integer, Film> filmsCopy = new HashMap<>(films);

        Comparator<Film> compareByLikes = Comparator.comparingInt(film -> film.getLikes().size());

        List<Film> topFilms = filmsCopy.values().stream()
                .sorted(compareByLikes.reversed())
                .limit(count)
                .collect(Collectors.toList());

        return topFilms;
    }

    @Override
    public Film findFilmByID(Integer filmID) {
        Optional<Film> filmOptional = films.values().stream()
                .filter(film -> film.getId().equals(filmID))
                .findFirst();

        return filmOptional.orElseThrow(() ->
                new EntityNotFoundException(Film.class, "Фильм с ID " + filmID + " не найден."));
    }

    @Override
    public Set<Long> getAllLikes(Integer filmID) {
        Film film = findFilmByID(filmID);
        if (film != null) {
            return film.getLikes();
        } else {
            log.warn("Фильм с id {} не найден.", filmID);
            throw new EntityNotFoundException(Film.class, "Фильм с ID " + filmID + " не найден.");
        }
    }


    private int generateID() {
        return ++generatedID;
    }

}