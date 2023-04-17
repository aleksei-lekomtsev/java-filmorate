package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    private Integer   id;

    @NotBlank(message = "Name must not be null and must contain at least one non-whitespace character.")
    private String    name;

    @NotNull(message = "Description must not be empty.")
    @Size(max = 200, message = "Size the description must be lower or equal to 200.")
    private String    description;

    @NotNull(message = "Release date must not be empty.")
    private LocalDate releaseDate;

    @Positive(message = "Duration must be a strictly positive number.")
    private Integer   duration;

    private final Mpa mpa = new Mpa();

    private final Collection<Genre> genres = new HashSet<>();
}
