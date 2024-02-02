package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
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


    public User(@Email @NotEmpty String email, @NotBlank @Pattern(regexp = "\\S*") String login,
                @NotNull @PastOrPresent LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

}