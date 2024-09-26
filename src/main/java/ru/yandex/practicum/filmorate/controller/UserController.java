package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final String userFriendsPath = "/{id}/friends/{friend-id}";

    @GetMapping("/{id}")
    public UserDto findUser(@PathVariable("id") Long userId) {
        return userService.findUser(userId);
    }

    @PutMapping(userFriendsPath)
    public void addFriend(@PathVariable("id") Long userId, @PathVariable("friend-id") Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping(userFriendsPath)
    public void deleteFriend(@PathVariable("id") Long userId, @PathVariable("friend-id") Long friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDto> findFriends(@PathVariable("id") Long userId) {
        return userService.findFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{other-id}")
    public Collection<UserDto> findOther(@PathVariable("id") Long userId, @PathVariable("other-id") Long otherId) {
        return userService.findOther(userId, otherId);
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody NewUserRequest user) {
        return userService.create(user);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UpdateUserRequest newUser) {
        return userService.update(newUser);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long userId) {
        return userService.delete(userId);
    }
}
