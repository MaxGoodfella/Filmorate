package ru.yandex.practicum.filmorate.model;

import lombok.*;

// import javax.validation.constraints.Email;
import java.time.LocalDate;


@Data
public class User {

    private Integer id;
    private String name;
    @NonNull
    private String email;

//    @Email
//    private String email;

    @NonNull
    private String login;
    @NonNull
    private LocalDate birthday;



    // public User(@Email String email, @NonNull String login, @NonNull LocalDate birthday) {
    public User(@NonNull String email, @NonNull String login, @NonNull LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

}