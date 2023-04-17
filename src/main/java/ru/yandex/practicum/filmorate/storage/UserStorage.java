package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User create(User user);
    User update(User user);
    Collection<User> findAll();
    User findById(Integer id);
    Collection<User> findByIds(Collection<Integer> ids);
}
