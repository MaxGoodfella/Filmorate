package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import java.time.LocalDate;


@Data
public class User {

    private Integer id;
    private String name;
//    @NonNull
//    private String email;

    @NonNull
    @Email
    private String email;

//    @NonNull
//    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
//            +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
//            +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
//            message="{invalid.email}")
//    String email;

    @NonNull
    private String login;
    @NonNull
    private LocalDate birthday;



     public User(@Email @NonNull String email, @NonNull String login, @NonNull LocalDate birthday) {
//    public User(@NonNull String email, @NonNull String login, @NonNull LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

}