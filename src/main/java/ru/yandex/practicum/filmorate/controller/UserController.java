package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.Validator.validateUser;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private static Integer id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST /users");

        validateUser(user);

        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT /users");

        validateUser(user);

        if(!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id: " + user.getId() + " не найден.");
        }

        users.put(user.getId(), user);
        return user;
    }
}
