package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User create(User user);

    User put(User updatedUser);

    List<User> findAll();

    User addFriend(Integer userId, Integer friendId);

    User removeFriend(Integer userId, Integer friendId);

    List<User> getAllFriends(Integer userID);

    List<User> getMutualFriends(Integer user1ID, Integer user2ID);

    User findUserByID(Integer userID);

}