package ru.skillbox.service;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skillbox.model.User;
import ru.skillbox.repository.UserRepository;

@Service
public class UserService {
    private static final String USER_ALREADY_EXISTS = "Пользователь с email = %s уже cуществует";
    private static final String NOT_FOUND = "Пользователь с email = %s уже cуществует";
    private static final String UPDATED = "Пользователь с фамилией %s успешно сохранен";
    private static final String DELETED = "Пользователь с id = %s удален";
    private static final String SUBSCRIBED = "Пользователь %s подписан на пользователя %s";
    private static final String DELETED_SUBSCRIPTION = "Подписка пользователя %s на пользователя %s удалена";
    private static final String NOT_SUBSCRIBED = "Пользователь %s не подписан на пользователя %s";
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (this.userRepository.existsUserByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, String.format(USER_ALREADY_EXISTS, user.getEmail()));
        } else {
            return this.userRepository.save(user);
        }
    }

    public User restoreUser(String email) {
        if (!this.userRepository.existsUserByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NOT_FOUND, email));
        } else {
            User restoreUser = this.userRepository.findUserByEmail(email);
            restoreUser.setDeleted(false);
            this.updateUser(restoreUser, restoreUser.getId());
            return restoreUser;
        }
    }

    public User getUser(long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public String updateUser(User user, long id) {
        if (!this.userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            User savedUser = (User) this.userRepository.save(user);
            return String.format(UPDATED, savedUser.getSecondName());
        }
    }

    public String deleteUser(long id) {
        if (!this.userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            this.userRepository.deleteById(id);
            return String.format(DELETED, id);
        }
    }

    public List<User> getUsers() {
        return this.userRepository.findUsersByDeleted(false);
    }

    public Set<User> getSubscribers(long id) {
        return this.userRepository.findById(id).map(User::getSubscribers).orElse(null);
    }

    public String subscribe(long subscriberId, long userId) {
        User user = this.getUser(userId);
        user.getSubscribers().add(this.getUser(subscriberId));
        this.updateUser(user, userId);
        return String.format(SUBSCRIBED, subscriberId, userId);
    }

    public String unSubscribe(long subscriberId, long userId) {
        User subscriber = this.getUser(subscriberId);
        User user = this.getUser(userId);
        Set<User> subscribers = user.getSubscribers();
        if (subscribers.contains(this.getUser(subscriberId))) {
            subscribers.remove(subscriber);
            this.updateUser(user, userId);
            return String.format(DELETED_SUBSCRIPTION, subscriberId, userId);
        } else {
            return String.format(NOT_SUBSCRIBED, subscriberId, userId);
        }
    }


}
