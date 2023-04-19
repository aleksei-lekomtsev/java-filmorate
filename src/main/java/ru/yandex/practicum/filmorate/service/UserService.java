package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage     userStorage;
    private final FriendDbStorage friendDbStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendDbStorage friendDbStorage) {
        this.userStorage = userStorage;
        this.friendDbStorage = friendDbStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User createUser(User user) {
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public User findUserById(Integer id) {
        return userStorage.findById(id);
    }

    public void addFriend(Integer id, Integer friendId) {
        User user   = userStorage.findById(id);
        User friend = userStorage.findById(friendId);
        friendDbStorage.create(user.getId(), friend.getId());
    }

    public void deleteFriend(Integer id, Integer friendId) {
        friendDbStorage.delete(id, friendId);
    }

    public Collection<User> findFriends(Integer id) {
        Collection<Friend> friends = friendDbStorage.findByUserId(id);
        return friends.isEmpty()
                ? new HashSet<>()
                : userStorage.findByIds(friends
                .stream()
                .map(Friend::getFriendId)
                .collect(Collectors.toList()));
    }

    public Collection<User> findCommonFriends(Integer id, Integer otherId) {
        Collection<User> friends      = findFriends(id);
        Collection<User> otherFriends = findFriends(otherId);
        Collection<User> result       = new HashSet<>(friends);
        result.retainAll(otherFriends);
        return result;
    }
}
