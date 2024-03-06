package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;
import lombok.Singular;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    private Integer id;

    @NotBlank
    private String name;

    @NotNull
    @Size(min = 1, max = 200)
    private String description;

    @NotNull
    @MinimumDate
    private LocalDate releaseDate;

    @NotNull
    @Positive
    private int duration;

    @Singular
    private final Set<Integer> likes = new HashSet<>();

    private FilmGenre genre;

    private FilmRating rating;


    public Film(@NotBlank String name, @NotNull @Size(min = 1, max = 200) String description, @NotNull @MinimumDate LocalDate releaseDate,
                @NotNull @Positive int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

}