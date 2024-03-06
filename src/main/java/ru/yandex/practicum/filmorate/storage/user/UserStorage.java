package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User put(User updatedUser);

    List<User> findAll();

    User addFriend(Integer userId, Integer friendId);

    User removeFriend(Integer userId, Integer friendId);

    List<User> getAllFriends(Integer userID);

    List<User> getMutualFriends(Integer user1ID, Integer user2ID);

    User findUserByID(Integer userID);

}