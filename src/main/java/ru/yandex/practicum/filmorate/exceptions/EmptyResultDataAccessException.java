package ru.yandex.practicum.filmorate.exceptions;

public class EmptyResultDataAccessException extends RuntimeException {

    public EmptyResultDataAccessException(Class<?> entityClass, String message) {
        super("Entity '" + entityClass.getSimpleName() + "' not found. " + message);
    }

}
