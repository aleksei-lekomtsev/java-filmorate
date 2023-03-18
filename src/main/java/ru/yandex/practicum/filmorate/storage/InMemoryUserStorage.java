package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static Integer            id    = 0;
    private final  Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if(!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователь с id: " + user.getId() + " не найден.");
        }

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findUserById(Integer id) {
        if(!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с id: " + id + " не найден.");
        }
        return users.get(id);
    }

    @Override
    public Collection<User> findUsersByIds(Collection<Integer> ids) {
        Map<Integer, User> users = new HashMap<> (this.users);
        users.keySet().retainAll(ids);
        return users.values();
    }
}
