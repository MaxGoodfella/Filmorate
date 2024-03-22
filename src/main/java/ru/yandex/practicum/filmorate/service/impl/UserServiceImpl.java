package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;


    @Override
    public User save(User newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public void saveMany(List<User> newUsers) {
        userRepository.saveMany(newUsers);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteById(Integer userID) {
        return userRepository.deleteById(userID);
    }

    @Override
    public boolean deleteAll() {
        return userRepository.deleteAll();
    }


//    private UserStorage userStorage;
//
//    @Override
//    public User create(User user) {
//        validateUser(user);
//        return userStorage.create(user);
//    }
//
//    @Override
//    public User put(User updatedUser) {
//        validateUser(updatedUser);
//        return userStorage.put(updatedUser);
//    }
//
//    @Override
//    public List<User> findAll() {
//        return userStorage.findAll();
//    }
//
//    @Override
//    public User addFriend(Integer userId, Integer friendId) {
//        return userStorage.addFriend(userId, friendId);
//    }
//
//    @Override
//    public User removeFriend(Integer userId, Integer friendId) {
//        return userStorage.removeFriend(userId, friendId);
//    }
//
//    @Override
//    public List<User> getAllFriends(Integer userID) {
//        return userStorage.getAllFriends(userID);
//    }
//
//    @Override
//    public List<User> getMutualFriends(Integer user1ID, Integer user2ID) {
//        return userStorage.getMutualFriends(user1ID, user2ID);
//    }
//
//    @Override
//    public User findUserByID(Integer userID) {
//        return userStorage.findUserByID(userID);
//    }
//
//
//    private void validateUser(User user) {
//        if (user.getName() == null || user.getName().isBlank()) {
//            log.info("Имя отсутствует, в качестве имени будет использован логин {}", user.getLogin());
//            user.setName(user.getLogin());
//        }
//    }

}