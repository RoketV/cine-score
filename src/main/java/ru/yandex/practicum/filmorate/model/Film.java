package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;


@Entity
@Data
public class Film {

    @Id
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private long rate;
}
