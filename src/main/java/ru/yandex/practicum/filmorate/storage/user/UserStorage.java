package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    User create(User user);

    User put(User updatedUser);

    Map<Integer, User> findAll();

    User addFriend(Integer userId, Integer friendId);

    User removeFriend(Integer userId, Integer friendId);

    List<User> getAllFriends(Integer userID);

    List<User> getMutualFriends(Integer user1ID, Integer user2ID);

    User findUserByID(Integer userID);

}