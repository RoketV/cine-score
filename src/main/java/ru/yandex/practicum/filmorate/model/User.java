package ru.yandex.practicum.filmorate.model;

import lombok.Data;


import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
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
    @ElementCollection
    private Set<Long> friends;
    @ElementCollection
    private Set<Long> likedFilms;
}
