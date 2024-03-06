package ru.yandex.practicum.filmorate.model.user;

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

    @Singular
    private final Set<Integer> friends = new HashSet<>();

    private UserFriendship friendship;

    public User(@Email @NotEmpty String email, @NotBlank @Pattern(regexp = "\\S*") String login,
                @NotNull @PastOrPresent LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

}