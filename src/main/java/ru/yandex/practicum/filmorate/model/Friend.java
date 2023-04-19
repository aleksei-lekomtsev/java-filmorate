package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friend {
    private Integer id;
    private Integer userId;
    private Integer friendId;
    private Boolean confirmed;
}
