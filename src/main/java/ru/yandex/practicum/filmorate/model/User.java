package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Singular;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


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

    // нижние пока закомментил
//    @Singular
//    private final Set<Integer> friends = new HashSet<>();
//
//    private UserFriendship friendship;

    public User(@Email @NotEmpty String email, @NotBlank @Pattern(regexp = "\\S*") String login,
                @NotNull @PastOrPresent LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

    public User(Integer id, String name, @Email @NotEmpty String email, @NotBlank @Pattern(regexp = "\\S*") String login,
                @NotNull @PastOrPresent LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }




    public User setId(Integer id) {
        this.id = id;
        return this;
    }
}


/*
Select f.film_id, g.genre_id
From films as f
JOIN film_genre as fg ON f.film_id = fg.film_id
JOIN genre as g ON fg.genre_id = g.genre_id
 */