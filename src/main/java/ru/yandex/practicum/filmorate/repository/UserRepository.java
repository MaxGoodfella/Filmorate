package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {

    User save(User newUser);

    void saveMany(List<User> newUsers);

    boolean update(User user);

    User findById(Integer id);

    User findByName(String userName);

    Integer findIdByName(String name);

    List<User> findAll();

    boolean deleteById(Integer userID);

    boolean deleteAll();

    // update

// add friend to user
    // remove friend from user
}
