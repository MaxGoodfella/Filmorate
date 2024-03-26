package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer id;

    private String name;

    @Email
    @NotEmpty
    private String email;

    @NotBlank
    @Pattern(regexp = "\\S*")
    private String login;

    @NotNull
    @PastOrPresent
    private LocalDate birthday;


    public User(String email, String login, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

}