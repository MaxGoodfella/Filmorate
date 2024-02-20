package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final List<Film> films = new ArrayList<>();

    private final InMemoryUserStorage inMemoryUserStorage;

    private int generatedID = 0;


    public InMemoryFilmStorage(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }


    @Override
    public Film create(Film film) {
        for (Film existingFilm : films) {
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

        films.add(film);
        log.info("Фильм успешно зарегистрирован. Название: {}", film.getName());
        return film;
    }

    @Override
    public Film put(Film updatedFilm) {
        int idToUpdate = updatedFilm.getId();

        for (Film film : films) {
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
    public List<Film> findAll() {
        return films;
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {

        Film film = findFilmByID(filmId);
        User user = inMemoryUserStorage.findUserByID(userId);

        if (film == null) {
            log.warn("Фильм с ID {} не найден.", filmId);
            throw new EntityNotFoundException(User.class, "Фильм с ID " + filmId + " не найден.");
        }

        if (user == null) {
            log.warn("Пользователь с ID {} не найден.", userId);
            throw new EntityNotFoundException(User.class, "Пользователь с ID " + userId + " не найден.");
        }

        Set<Long> listOfFans = film.getLikes();
        long idToAdd = user.getId();

        if (listOfFans.contains(idToAdd)) {
            log.warn("Пользователь с электронной почтой {} уже добавил фильм с названием {} в понравившиеся.",
                    user.getEmail(), film.getName());
            throw new EntityAlreadyExistsException(User.class, "Пользователь с электронной почтой " +
                    user.getEmail() + " уже добавил в список понравившихся фильм с названием " +
                    film.getName() + ".");
        }

        listOfFans.add(idToAdd);
        log.info("Фильм с названием {} успешно добавлен в список понравившихся фильмов пользователя {}.",
                film.getName(), user.getEmail());
        return film;

    }

    @Override
    public Film removeLike(Integer filmId, Integer userId) {

        Film film = findFilmByID(filmId);
        User user = inMemoryUserStorage.findUserByID(userId);

        if (film == null) {
            log.warn("Фильм с ID {} не найден.", filmId);
            throw new EntityNotFoundException(User.class, "Фильм с ID " + filmId + " не найден.");
        }

        if (user == null) {
            log.warn("Пользователь с ID {} не найден.", userId);
            throw new EntityNotFoundException(User.class, "Пользователь с ID " + userId + " не найден.");
        }

        Set<Long> listOfFans = film.getLikes();
        long idToRemove = user.getId();

        for (Long fansIDs : listOfFans) {
            if (!listOfFans.contains(idToRemove)) {
                log.warn("Фильм с названием {} не найден в списке понравившихся фильмов пользователя с " +
                                "электронной почтой {}.", film.getName(), user.getEmail());
                throw new EntityNotFoundException(User.class, "Фильм с названием " + film.getName() +
                        " не найден в списке понравившихся фильмов пользователя с электронной почтой " + user.getEmail()
                        + ".");
            }
        }
        listOfFans.remove(idToRemove);
        log.info("Фильм с названием {} успешно удалён из списка понравившихся фильмов пользователя {}.",
                film.getName(), user.getEmail());
        return film;

    }

    @Override
    public List<Film> getTop10ByLikes(Integer count) {

        List<Film> filmsCopy = new ArrayList<>(films);

        Comparator<Film> compareByLikes = Comparator.comparingInt(film -> film.getLikes().size());

        List<Film> topFilms = filmsCopy.stream()
                .sorted(compareByLikes.reversed())
                .limit(count)
                .collect(Collectors.toList());

        return topFilms;

    }

    @Override
    public Film findFilmByID(Integer filmID) {
        for (Film film : films) {
            if (film.getId().equals(filmID)) {
                return film;
            }
        }
        throw new EntityNotFoundException(Film.class, "Фильм с ID " + filmID + " не найден.");
    }


    public Set<User> getAllLikes(Integer filmID) {
        Film film = findFilmByID(filmID);

        if (film != null) {
            Set<User> filmFans = new HashSet<>();
            Set<Long> fansIds = film.getLikes();

            List<User> allUsers = inMemoryUserStorage.findAll();

            for (Long fanId : fansIds) {
                for (User user : allUsers) {
                    if (user.getId().equals(fanId.intValue())) {
                        filmFans.add(user);
                        break;
                    }
                }
            }
            return filmFans;
        } else {
            log.warn("Фильм с id {} не найден.", filmID);
            throw new EntityNotFoundException(User.class, "Фильм с ID " + filmID + " не найден.");
        }
    }

    private int generateID() {
        return ++generatedID;
    }

}