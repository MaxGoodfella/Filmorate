package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {

    private Integer id;

    @NotBlank
    private String name;

    public Genre setId(Integer id) {
        this.id = id;
        return this;
    }
}
