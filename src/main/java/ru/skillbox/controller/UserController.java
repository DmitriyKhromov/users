package ru.skillbox.controller;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.skillbox.service.UserService;
import ru.skillbox.model.User;

@AllArgsConstructor
@RestController
@RequestMapping({"/users"})
public class UserController {
    private static final String CREATE_SUCCESS = "Пользователь %s c id =  %d создан";
    private static final String USER_ID_ERROR = "id %d пользователя в строке запроса не соответствует id %d пользователя в теле сообщения";
    private final UserService userService;

    @PostMapping
    String createUser(@RequestBody User user) {
        User createdUser = this.userService.createUser(user);
        return String.format(CREATE_SUCCESS, createdUser.getFirstName(), createdUser.getId());
    }

    @GetMapping({"/{id}"})
    User getUser(@PathVariable long id) {
        return this.userService.getUser(id);
    }

    @PostMapping({"/{id}"})
    String updateUser(@RequestBody User user, @PathVariable long id) {
        if (id != user.getId())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(USER_ID_ERROR, id, user.getId()));
        else
            return this.userService.updateUser(user, id);
    }

    @GetMapping({"/restore/{email}"})
    User updateUser(@PathVariable String email) {
        return this.userService.restoreUser(email);
    }

    @DeleteMapping({"/{id}"})
    String deleteUser(@PathVariable long id) {
        return this.userService.deleteUser(id);
    }

    @GetMapping
    List<User> getUsers() {
        return this.userService.getUsers();
    }

    @GetMapping({"/{id}/subscribers"})
    Set<User> getSubscribers(@PathVariable long id) {
        return this.userService.getSubscribers(id);
    }

    @GetMapping({"/{subscriberId}/subscribe/{userId}"})
    String subscribe(@PathVariable long subscriberId, @PathVariable long userId) {
        return this.userService.subscribe(subscriberId, userId);
    }

    @GetMapping({"/{subscriberId}/unsubscribe/{userId}"})
    String unSubscribe(@PathVariable long subscriberId, @PathVariable long userId) {
        return this.userService.unSubscribe(subscriberId, userId);
    }
}
