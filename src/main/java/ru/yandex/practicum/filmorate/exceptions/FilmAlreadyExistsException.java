package ru.yandex.practicum.filmorate.exceptions;

public class FilmAlreadyExistsException extends RuntimeException {
    public FilmAlreadyExistsException(String s) {
        super(s);
    }

}
