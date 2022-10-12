package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;


@Entity
@Data
public class Film {

    public Film() {
        idCounter++;
        id = idCounter;
    }


    private static long idCounter = 0;
    @Id
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
