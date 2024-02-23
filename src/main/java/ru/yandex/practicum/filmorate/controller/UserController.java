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
    public User create(@Valid @RequestBody User newUser) {
        return userService.create(newUser);
    }

    @PutMapping
    public User put(@Valid @RequestBody User updatedUser) {
        return userService.put(updatedUser);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Object[] findAll() {
        return userService.findAll().values().toArray();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) {
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping(value = "/{id}/friends", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllFriends(@PathVariable("id") Integer userID) {
        return userService.getAllFriends(userID);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getMutualFriends(@PathVariable("id") Integer user1ID, @PathVariable("otherId") Integer user2ID) {
        return userService.getMutualFriends(user1ID, user2ID);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User findUserByID(@PathVariable("id") Integer userID) {
        return userService.findUserByID(userID);
    }

}