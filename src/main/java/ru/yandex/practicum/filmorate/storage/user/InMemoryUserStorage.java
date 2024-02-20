package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final List<User> users = new ArrayList<>();

    private int generatedID = 0;


    @Override
    public User create(User newUser) {
        validateUser(newUser);

        for (User existingUser : users) {
            if (existingUser.getEmail().equals(newUser.getEmail())) {
                log.warn("Регистрация пользователя не удалась. Пользователь с электронной почтой {} уже существует.",
                        newUser.getEmail());
                throw new EntityAlreadyExistsException(User.class, "Пользователь с электронной почтой " +
                        newUser.getEmail() + " уже зарегистрирован.");
            }
            if (existingUser.getLogin().equals(newUser.getLogin())) {
                log.warn("Регистрация пользователя не удалась. Пользователь с логином {} уже существует.",
                        newUser.getLogin());
                throw new EntityAlreadyExistsException(User.class, "Пользователь с логином " +
                        newUser.getLogin() + " уже зарегистрирован.");
            }
        }

        newUser.setId(generateID());
        users.add(newUser);

        log.info("Пользователь успешно зарегистрирован. Email: {}", newUser.getEmail());
        return newUser;
    }

    @Override
    public User put(User updatedUser) {
        validateUser(updatedUser);

        int idToUpdate = updatedUser.getId();

        for (User user : users) {
            if (user.getId() == idToUpdate) {
                user.setEmail(updatedUser.getEmail());
                user.setName(updatedUser.getName());
                user.setBirthday(updatedUser.getBirthday());
                user.setLogin(updatedUser.getLogin());

                log.info("Информация о пользователе с id {} успешно обновлена.", idToUpdate);
                return user;
            }
        }

        log.warn("Пользователь c id {} для обновления не найден.", idToUpdate);
        throw new EntityNotFoundException(User.class, "Пользователь с id " + idToUpdate + " не найден.");
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        User user = findUserByID(userId);
        User usersFriend = findUserByID(friendId);

        if (user == null) {
            log.warn("Пользователь с ID {} не найден.", userId);
            throw new EntityNotFoundException(User.class, "Пользователь с ID " + userId + " не найден.");
        }

        if (usersFriend == null) {
            log.warn("Пользователь с ID {} не найден.", friendId);
            throw new EntityNotFoundException(User.class, "Пользователь с ID " + friendId + " не найден.");
        }

        Set<Long> listOfFriends1 = user.getFriends();
        Set<Long> listOfFriends2 = usersFriend.getFriends();
        long idToAdd1 = usersFriend.getId();
        long idToAdd2 = user.getId();

        for (Long friendID : listOfFriends1) {
            if (friendID.equals(idToAdd1)) {
                User existingFriend = findUserByID(friendID.intValue());
                if (!existingFriend.equals(usersFriend)) {
                    existingFriend.setName(usersFriend.getName());
                    existingFriend.setEmail(usersFriend.getEmail());
                    existingFriend.setBirthday(usersFriend.getBirthday());
                    existingFriend.setLogin(usersFriend.getLogin());
                    log.info("Информация о пользователе {} обновлена в списке друзей пользователя {}.",
                            usersFriend.getEmail(), user.getEmail());
                }
                return existingFriend;
            }
        }

        listOfFriends1.add(idToAdd1);
        log.info("Пользователь {} успешно добавлен в список друзей пользователя {}.",
                usersFriend.getEmail(), user.getEmail());

        listOfFriends2.add(idToAdd2);
        log.info("Пользователь {} успешно добавлен в список друзей пользователя {}.",
                user.getEmail(), usersFriend.getEmail());

        return usersFriend;
    }

    @Override
    public User removeFriend(Integer userId, Integer friendId) {

        User user = findUserByID(userId);
        User usersExFriend = findUserByID(friendId);

        if (user == null) {
            log.warn("Пользователь с ID {} не найден.", userId);
            throw new EntityNotFoundException(User.class, "Пользователь с ID " + userId + " не найден.");
        }

        if (usersExFriend == null) {
            log.warn("Пользователь с ID {} не найден.", friendId);
            throw new EntityNotFoundException(User.class, "Пользователь с ID " + friendId + " не найден.");
        }

        Set<Long> listOfFriends1 = user.getFriends();
        Set<Long> listOfFriends2 = usersExFriend.getFriends();
        long idToRemove1 = usersExFriend.getId();
        long idToRemove2 = user.getId();

        if (!listOfFriends1.contains(idToRemove1)) {
            log.warn("Пользователь электронной почтой {} не найден в списке друзей пользователя с почтой {}.",
                    usersExFriend.getEmail(), user.getEmail());
            throw new EntityNotFoundException(User.class, "Пользователь с электронной почтой " +
                    usersExFriend.getEmail() + " не найден в списке друзей пользователя с электронной почтой " +
                    user.getEmail() + ".");
        }

        listOfFriends1.remove(idToRemove1);
        log.info("Пользователь {} успешно удалён из списка друзей пользователя {}.",
                usersExFriend.getEmail(), user.getEmail());

        listOfFriends2.remove(idToRemove2);
        log.info("Пользователь {} успешно удалён из списка друзей пользователя {}.",
                user.getEmail(), usersExFriend.getEmail());

        return usersExFriend;

    }

    @Override
    public List<User> getAllFriends(Integer userID) {

        User user = findUserByID(userID);

        if (user != null) {
            Set<Long> friendIds = user.getFriends();
            List<User> userFriends = new ArrayList<>();

            for (Long friendId : friendIds) {
                for (User friend : users) {
                    if (friend.getId().equals(friendId.intValue())) {
                        userFriends.add(friend);
                        break;
                    }
                }
            }

            return userFriends;
        } else {
            log.warn("Пользователь с id {} не найден.", userID);
            throw new EntityNotFoundException(User.class, "Пользователь с ID " + userID + " не найден.");
        }
    }

    @Override
    public List<User> getMutualFriends(Integer user1ID, Integer user2ID) {

        User user1 = findUserByID(user1ID);
        User user2 = findUserByID(user2ID);

        if (user1 != null && user2 != null) {

            List<User> user1Friends = getAllFriends(user1ID);
            List<User> user2Friends = getAllFriends(user2ID);

            user1Friends.retainAll(user2Friends);

            return user1Friends;

        } else {
            log.warn("Один из пользователей с id {} или {} не найден.", user1ID, user2ID);
            throw new EntityNotFoundException(User.class, "Один из пользователей с ID " + user1ID + " или "
                    + user2ID + " не найден.");
        }

    }

    @Override
    public User findUserByID(Integer userID) {
        for (User user : users) {
            if (user.getId().equals(userID)) {
                return user;
            }
        }
        throw new EntityNotFoundException(User.class, "Пользователь с ID " + userID + " не найден.");
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя отсутствует, в качестве имени будет использован логин {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }

    private int generateID() {
        return ++generatedID;
    }

}