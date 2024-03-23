package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
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
        String userName = newUser.getName();
        Integer existingUserId = userRepository.findIdByName(userName);
        if (existingUserId != null) {
            newUser.setId(existingUserId);
            return newUser;
        } else {
            return userRepository.save(newUser);
        }
    }

    @Override
    public void saveMany(List<User> newUsers) {
        for (User newUser : newUsers) {
            Integer existingUserId = userRepository.findIdByName(newUser.getName());
            if (existingUserId != null) {
                newUser.setId(existingUserId);
                update(newUser);
                System.out.println("User '" + newUser.getName() + "' has been updated.");
            } else {
                save(newUser);
                System.out.println("User '" + newUser.getName() + "' has been added.");
            }
        }
    }

    @Override
    public void update(User user) {
        boolean isSuccess = userRepository.update(user);

        if (!isSuccess) {
            throw new EntityNotFoundException(User.class,
                    "User with id = " + user.getId() + " hasn't been found");
        }
    }

    @Override
    public User findById(Integer id) {
        try {
            return userRepository.findById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(User.class, "User with id = " + id + " hasn't been found");
        }
    }

    @Override
    public User findByName(String userName) {
        try {
            return userRepository.findByName(userName);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(User.class, "User with name '" + userName + "' hasn't been found");
        }
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