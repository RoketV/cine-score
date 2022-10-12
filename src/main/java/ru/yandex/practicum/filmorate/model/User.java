package ru.yandex.practicum.filmorate.model;

import lombok.Data;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;


@Entity
@Data
public class User {

    public User() {
        idCounter++;
        id = idCounter;
    }


    private static long idCounter = 0;
    @Id
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

}
