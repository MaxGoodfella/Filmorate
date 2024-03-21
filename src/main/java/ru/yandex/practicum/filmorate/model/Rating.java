package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Rating {

    private Integer id;

    @NotBlank
    private String name;

    public Rating(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Rating setId(Integer id) {
        this.id = id;
        return this;
    }
}
