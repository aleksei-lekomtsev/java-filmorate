package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component("inMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private static Integer            id    = 0;
    private final  Map<Integer, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        if (user == null) {
            log.warn("Произошла непредвиденная ошибка. Значение user не может быть null");
            throw new RuntimeException("Произошла непредвиденная ошибка. Значение user не может быть null");
        }
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (user == null) {
            log.warn("Произошла непредвиденная ошибка. Значение user не может быть null");
            throw new RuntimeException("Произошла непредвиденная ошибка. Значение user не может быть null");
        }
        if(!users.containsKey(user.getId())) {
            log.warn("Пользователь с id: " + user.getId() + " не найден.");
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
    public User findById(Integer id) {
        if(!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с id: " + id + " не найден.");
        }
        return users.get(id);
    }

    @Override
    public Collection<User> findByIds(Collection<Integer> ids) {
        Map<Integer, User> users = new HashMap<> (this.users);
        users.keySet().retainAll(ids);
        return users.values();
    }
}
