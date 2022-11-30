package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDate;
import java.util.LinkedHashSet;


//@Entity
@Data
public class Film {

    @Id
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;
    private int duration;
    private long rate;
}
