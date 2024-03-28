package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
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

    private Integer rating;

    private Integer popularity;


    public Film(String name, String description, LocalDate releaseDate, int duration, Integer rating, Integer popularity) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rating = rating;
        this.popularity = popularity;
    }


    public Film(String name, String description, LocalDate releaseDate, int duration, Integer popularity) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.popularity = popularity;
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration, Integer popularity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.popularity = popularity;
    }

}