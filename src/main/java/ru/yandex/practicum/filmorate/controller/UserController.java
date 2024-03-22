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

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User findByID(@PathVariable("id") Integer userID) {
        log.info("Start fetching user with id = {}", userID);
        User fetchedUser = userService.findById(userID);
        log.info("Finish fetching user with id = {}", fetchedUser.getId());
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





//    private UserService userService;
//
//
//    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public User create(@Valid @RequestBody User newUser) {
//        return userService.create(newUser);
//    }
//
//    @PutMapping
//    public User put(@Valid @RequestBody User updatedUser) {
//        return userService.put(updatedUser);
//    }
//
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<User> findAll() {
//        return userService.findAll();
//    }
//
//    @PutMapping("/{id}/friends/{friendId}")
//    public User addFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) {
//        return userService.addFriend(userId, friendId);
//    }
//
//    @DeleteMapping("/{id}/friends/{friendId}")
//    public User removeFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) {
//        return userService.removeFriend(userId, friendId);
//    }
//
//    @GetMapping(value = "/{id}/friends", produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<User> getAllFriends(@PathVariable("id") Integer userID) {
//        return userService.getAllFriends(userID);
//    }
//
//    @GetMapping(value = "/{id}/friends/common/{otherId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<User> getMutualFriends(@PathVariable("id") Integer user1ID, @PathVariable("otherId") Integer user2ID) {
//        return userService.getMutualFriends(user1ID, user2ID);
//    }
//
//    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public User findUserByID(@PathVariable("id") Integer userID) {
//        return userService.findUserByID(userID);
//    }

}