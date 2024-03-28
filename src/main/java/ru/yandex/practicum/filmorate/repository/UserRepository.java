package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {

    User save(User newUser);

    void saveMany(List<User> newUsers);

    boolean update(User user);

    User findById(Integer id);

    User findByName(String userName);

    User findByEmail(String email);

    User findByLogin(String login);

    Integer findIdByName(String name);

    List<User> findAll();

    boolean deleteById(Integer userID);

    boolean deleteAll();

    void addFriend(Integer userId, Integer friendId);

    boolean removeFriend(Integer userId, Integer friendId);

    List<Integer> findFriendsIdsById(Integer userId);

    List<Integer> getCommonFriends(Integer user1ID, Integer user2ID);

}