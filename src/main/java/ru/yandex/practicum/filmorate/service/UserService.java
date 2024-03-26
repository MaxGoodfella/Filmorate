package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User save(User newUser);

    void saveMany(List<User> newUsers);

    void update(User user);

    User findById(Integer id);

    User findByName(String userName);

    List<User> findAll();

    boolean deleteById(Integer userID);

    boolean deleteAll();

    void addFriend(Integer userId, Integer friendId);

    List<Integer> findUsersFriendsIds(Integer userId);

    boolean removeFriend(Integer userId, Integer friendId);

    List<Integer> getCommonFriends(Integer user1ID, Integer user2ID);

}