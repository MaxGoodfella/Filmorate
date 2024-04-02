package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private UserService userService;


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User save(@Valid @RequestBody User newUser) {
        log.info("Start saving user {}", newUser);
        User savedUser = userService.save(newUser);
        log.info("Finish saving user {}", savedUser);
        return savedUser;
    }

    @PostMapping(value = "/multiple", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveMany(@Valid @RequestBody List<User> newUsers) {
        log.info("Start saving users {}", newUsers);
        userService.saveMany(newUsers);
        log.info("Finish saving users {}", newUsers);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Start updating user {}", user);
        User updatedUser = userService.update(user);
        log.info("Finish updating user {}", user);
        return updatedUser;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User findByID(@PathVariable("id") Integer userID) {
        log.info("Start fetching user with id = {}", userID);
        User fetchedUser = userService.findById(userID);
        log.info("Finish fetching user with id = {}", fetchedUser.getId());
        return fetchedUser;
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User findByName(@PathVariable("name") String userName) {
        log.info("Start fetching user with name = {}", userName);
        User fetchedUser = userService.findByName(userName);
        log.info("Finish fetching user with name = {}", userName);
        return fetchedUser;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> findAll() {
        log.info("Start fetching all users");
        List<User> fetchedUsers = userService.findAll();
        log.info("Finish fetching all users");
        return fetchedUsers;
    }

    @DeleteMapping(value = "/{id}")
    public boolean deleteById(@PathVariable("id") Integer userID) {
        log.info("Start deleting user with id = {}", userID);
        boolean isDeleted = userService.deleteById(userID);
        log.info("Finish deleting user with id = {}", userID);
        return isDeleted;
    }

    @DeleteMapping
    public boolean deleteAll() {
        log.info("Start deleting all users");
        boolean areDeleted = userService.deleteAll();
        log.info("Finish deleting all users");
        return areDeleted;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) {
        log.info("Start adding friend with id = {} to user with id = {}", friendId, userId);
        userService.addFriend(userId, friendId);
        log.info("Finish adding friend with id = {} to user with id = {}", friendId, userId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public boolean removeFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) {
        log.info("Start removing friend with id = {} from user with id = {}", friendId, userId);
        boolean isRemoved = userService.removeFriend(userId, friendId);
        log.info("Finish removing friend with id = {} from user with id = {}", friendId, userId);
        return isRemoved;
    }

    @GetMapping(value = "/{id}/friends", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> findFriendsById(@PathVariable("id") Integer userID) {
        log.info("Start fetching all friends of user with id = {}", userID);
        List<User> fetchedUsers = userService.findFriendsById(userID);
        log.info("Finish fetching all friends of user with id = {}", userID);
        return fetchedUsers;
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getCommonFriends(@PathVariable("id") Integer user1ID, @PathVariable("otherId") Integer user2ID) {
        log.info("Start fetching common friends of users with id = {} and id = {}", user1ID, user2ID);
        List<User> fetchedUsers = userService.getCommonFriends(user1ID, user2ID);
        log.info("Finish fetching common friends of users with id = {} and id = {}", user1ID, user2ID);
        return fetchedUsers;
    }

}