package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {

    User findById(Integer id);

    List<User> findAll();

    User save(User newUser);

    void saveMany(List<User> newUsers);

    boolean deleteById(Integer userID);

    boolean deleteAll();

    // update

// add friend to user
    // remove friend from user
}
