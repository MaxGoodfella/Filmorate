package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Data
public class Film {

    private Integer id;

    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @MinimumDate
    private LocalDate releaseDate;

    @Positive
    private Integer duration;


    public Film(@NotBlank String name, @Size(min = 1, max = 200) String description, @MinimumDate LocalDate releaseDate,
                @Positive Integer duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

}