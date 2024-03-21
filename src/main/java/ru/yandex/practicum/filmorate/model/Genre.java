package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class Genre {

    private Integer id;

    @NotBlank
    private String name;


    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre setId(Integer id) {
        this.id = id;
        return this;
    }
}
