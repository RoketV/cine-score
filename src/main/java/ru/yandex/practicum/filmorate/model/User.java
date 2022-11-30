package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;



@Data
public class User {

    public User() {
        friends = new HashSet<>();
        likedFilms = new HashSet<>();
    }

    @Id
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;
    private Set<Long> likedFilms;
}
