package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User save(User newUser);

    User update(User user);

    User findById(Integer id);

    User findByName(String userName);

    List<User> findAll();

    boolean deleteById(Integer userID);

    boolean deleteAll();

    void addFriend(Integer userId, Integer friendId);

    List<User> findFriendsById(Integer userId);

    boolean removeFriend(Integer userId, Integer friendId);

    List<User> getCommonFriends(Integer user1ID, Integer user2ID);

}