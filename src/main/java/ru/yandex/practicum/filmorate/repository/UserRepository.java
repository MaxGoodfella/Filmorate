package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {

    User save(User newUser);

    void saveMany(List<User> newUsers);

    User findById(Integer id);

    List<User> findAll();

    boolean deleteById(Integer userID);

    boolean deleteAll();

    // update

// add friend to user
    // remove friend from user
}
