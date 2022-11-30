CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id int AUTO_INCREMENT PRIMARY KEY,
    mpa    varchar(50)
);

CREATE TABLE IF NOT EXISTS films
(
    film_id      int PRIMARY KEY AUTO_INCREMENT,
    title        varchar(50),
    description  varchar(50),
    release_date date,
    duration     int,
    rate         int
);

CREATE TABLE IF NOT EXISTS genres
(
    genre_id   int AUTO_INCREMENT PRIMARY KEY,
    genre_name varchar(50)
);

CREATE TABLE IF NOT EXISTS films_genres
(
    film_id  int REFERENCES films (film_id),
    genre_id int REFERENCES genres (genre_id)
);

CREATE TABLE IF NOT EXISTS statuses
(
    status_id int AUTO_INCREMENT PRIMARY KEY,
    status    varchar(50)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  int PRIMARY KEY AUTO_INCREMENT,
    email    varchar(50),
    login    varchar(50),
    name     varchar(50),
    birthday date
);

CREATE TABLE IF NOT EXISTS mpa_film
(
    film_id int REFERENCES films (film_id),
    mpa_id  int REFERENCES mpa (mpa_id)
);

CREATE TABLE IF NOT EXISTS friendship_relationships
(
    user_id              int REFERENCES users (user_id),
    friend_id            int REFERENCES users (user_id),
    friendship_status_id int REFERENCES statuses (status_id)
);

CREATE TABLE IF NOT EXISTS liked_films
(
    user_id int REFERENCES userS (user_id),
    film_id int REFERENCES films (film_id)
)


