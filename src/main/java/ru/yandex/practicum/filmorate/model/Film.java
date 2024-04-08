package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Positive
    private int duration;

    @NotNull
    private Rating mpa;

    private Integer popularity;

    private List<Genre> genres = new ArrayList<>();


    public Film(String name, String description, LocalDate releaseDate, int duration, Rating mpa, Integer popularity,
                List<Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.popularity = popularity;
        this.genres = genres;
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration, Rating mpa,
                Integer popularity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.popularity = popularity;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration, Rating mpa, Integer popularity) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.popularity = popularity;
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

}