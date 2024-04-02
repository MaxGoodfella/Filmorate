package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    private Integer id;

    @NotBlank
    private String name;


    public Rating setId(Integer id) {
        this.id = id;
        return this;
    }

}