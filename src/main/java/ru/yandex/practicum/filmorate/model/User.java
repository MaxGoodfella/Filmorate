package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


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

    // нижние пока закомментил
//    @Singular
//    private final Set<Integer> friends = new HashSet<>();
//
//    private UserFriendship friendship;



    public User setId(Integer id) {
        this.id = id;
        return this;
    }
}