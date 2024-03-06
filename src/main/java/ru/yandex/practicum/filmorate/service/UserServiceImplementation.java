package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {

    private UserStorage userStorage;

    @Override
    public User create(User user) {
        validateUser(user);
        return userStorage.create(user);
    }

    @Override
    public User put(User updatedUser) {
        validateUser(updatedUser);
        return userStorage.put(updatedUser);
    }

    @Override
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        return userStorage.addFriend(userId, friendId);
    }

    @Override
    public User removeFriend(Integer userId, Integer friendId) {
        return userStorage.removeFriend(userId, friendId);
    }

    @Override
    public List<User> getAllFriends(Integer userID) {
        return userStorage.getAllFriends(userID);
    }

    @Override
    public List<User> getMutualFriends(Integer user1ID, Integer user2ID) {
        return userStorage.getMutualFriends(user1ID, user2ID);
    }

    @Override
    public User findUserByID(Integer userID) {
        return userStorage.findUserByID(userID);
    }


    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя отсутствует, в качестве имени будет использован логин {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }

}