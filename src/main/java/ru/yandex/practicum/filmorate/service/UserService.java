package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User findUserById(Integer id) {
        return userStorage.findUserById(id);
    }

    public void addFriend(Integer id, Integer friendId) {
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
    }

    public Collection<User> findFriends(Integer id) {
        return userStorage.findUsersByIds(userStorage.findUserById(id).getFriends());
    }

    public Collection<User> findCommonFriends(Integer id, Integer otherId) {
        Set<Integer> friends = userStorage.findUserById(id).getFriends();
        Set<Integer> otherFriends = userStorage.findUserById(otherId).getFriends();
        Set<Integer> commonFriends = new HashSet<>(friends);
        commonFriends.retainAll(otherFriends);
        return userStorage.findUsersByIds(commonFriends);
    }
}
