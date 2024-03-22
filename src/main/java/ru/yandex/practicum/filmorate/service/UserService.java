package ru.yandex.practicum.filmorate.service;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserService {

    User save(User newUser);

    void saveMany(List<User> newUsers);

    User findById(Integer id);

    List<User> findAll();

    boolean deleteById(Integer userID);

    boolean deleteAll();




//    User create(User user);
//
//    User put(User updatedUser);
//
//    List<User> findAll();
//
//    User addFriend(Integer userId, Integer friendId);
//
//    User removeFriend(Integer userId, Integer friendId);
//
//    List<User> getAllFriends(Integer userID);
//
//    List<User> getMutualFriends(Integer user1ID, Integer user2ID);
//
//    User findUserByID(Integer userID);

}