package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;


    @Override
    public User save(User newUser) {
        validateUser(newUser);

        User existingUserByEmail = userRepository.findByEmail(newUser.getEmail());
        if (existingUserByEmail != null && existingUserByEmail.getId() != newUser.getId()) {
            throw new EntityAlreadyExistsException(User.class,
                    "User with email '" + newUser.getEmail() + "' already exists");
        }

        User existingUserByLogin = userRepository.findByLogin(newUser.getLogin());
        if (existingUserByLogin != null && existingUserByLogin.getId() != newUser.getId()) {
            throw new EntityAlreadyExistsException(User.class,
                    "User with login '" + newUser.getLogin() + "' already exists");
        }

        return userRepository.save(newUser);
    }

    @Override
    public void saveMany(List<User> newUsers) {

        List<User> existingUsers = new ArrayList<>();
        List<User> newUsersToSave = new ArrayList<>();

        for (User newUser : newUsers) {
            validateUser(newUser);

            User existingUserByEmail = userRepository.findByEmail(newUser.getEmail());
            User existingUserByLogin = userRepository.findByLogin(newUser.getLogin());

            if (existingUserByEmail != null || existingUserByLogin != null) {
                if (existingUserByEmail != null) {
                    existingUsers.add(existingUserByEmail);
                } else {
                    existingUsers.add(existingUserByLogin);
                }
            } else {
                newUsersToSave.add(newUser);
            }
        }

        if (!newUsersToSave.isEmpty()) {
            userRepository.saveMany(newUsersToSave);
        }

    }

    @Override
    public void update(User user) {
        validateUser(user);

        boolean isSuccess = userRepository.update(user);

        if (!isSuccess) {
            throw new EntityNotFoundException(User.class,
                    "User with id = " + user.getId() + " hasn't been found");
        }
    }

    @Override
    public User findById(Integer id) {
        if (userRepository.findById(id) == null) {
            throw new EntityNotFoundException(User.class, "User with id = " + id + " hasn't been found");
        }

        return userRepository.findById(id);
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

    @Override
    public void addFriend(Integer userId, Integer friendId) {

        User user = userRepository.findById(userId);
        User friend = userRepository.findById(friendId);
        List<Integer> usersFriendsIds = userRepository.findFriendsIdsById(userId);

        if (user == null) {
            throw new EntityNotFoundException(User.class, "User with id = " + userId + " hasn't been found");
        }

        if (friend == null) {
            throw new EntityNotFoundException(User.class, "Friend with id = " + friendId + " hasn't been found");
        }

        if (user.getId().equals(friend.getId())) {
            throw new IllegalArgumentException("user_ID matches friend_ID");
        }

        if (usersFriendsIds.contains(friendId)) {
            throw new EntityAlreadyExistsException(Integer.class, "Friend with id = " + friendId +
                    " is already in the list of friends of user with id = " + userId);
        }

        userRepository.addFriend(userId, friendId);

    }

    @Override
    public List<Integer> findFriendsIdsById(Integer userId) {
        if (userRepository.findById(userId) == null) {
            throw new EntityNotFoundException(User.class, "User with id = " + userId + " hasn't been found");
        }

        return userRepository.findFriendsIdsById(userId);
    }

    @Override
    public boolean removeFriend(Integer userId, Integer friendId) {

        User user = userRepository.findById(userId);
        User friend = userRepository.findById(friendId);
        List<Integer> usersFriendsIds = userRepository.findFriendsIdsById(userId);

        if (user == null) {
            throw new EntityNotFoundException(User.class, "User with id = " + userId + " hasn't been found");
        }

        if (friend == null) {
            throw new EntityNotFoundException(User.class, "Friend with id = " + friendId + " hasn't been found");
        }

        if (!usersFriendsIds.contains(friendId)) {
            throw new EntityNotFoundException(Integer.class, "Friend with id = " + friendId +
                    " is not yet in the list of friends of user with id = " + userId);
        }

        return userRepository.removeFriend(userId, friendId);

    }

    @Override
    public List<Integer> getCommonFriends(Integer user1ID, Integer user2ID) {

        User user1 = userRepository.findById(user1ID);
        User user2 = userRepository.findById(user2ID);

        if (user1 == null) {
            throw new EntityNotFoundException(User.class, "User with id = " + user1ID + " hasn't been found");
        }

        if (user2 == null) {
            throw new EntityNotFoundException(User.class, "User with id = " + user2ID + " hasn't been found");
        }

        if (user1.getId().equals(user2.getId())) {
            throw new IllegalArgumentException("First user_ID matches second user_ID");
        }

        return userRepository.getCommonFriends(user1ID, user2ID);
    }


    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя отсутствует, в качестве имени будет использован логин {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }

}