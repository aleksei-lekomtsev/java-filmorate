package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;


@Data
public class User {
    private Integer id;

    @NotEmpty
    @Email
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$", message = "login must be of 6 to 12 length with no special characters")
    private String login;

    private String name;

    @NotNull
    @PastOrPresent
    private LocalDate birthday;
}
