package ru.yandex.practicum.filmorate.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.guide.Genre;
import ru.yandex.practicum.filmorate.guide.MPA;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class FilmDbStorageImpl implements FilmDbStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorageImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFilm(Film film) { //todo: 1.4. POST .../films - добавление фильма
        //INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID, GENRE_ID, RATE)
        //VALUES ('New film', 'New film about friends', '1999-04-30', 120, 2, 3, 4);
        final String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID, GENRE_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stm.setString(1, film.getName());
            stm.setString(2, film.getDescription());
            stm.setString(3, film.getReleaseDate());
            stm.setLong(4, film.getDuration());
            final MPA MPA_ID = film.getMpa();
            //  "mpa": { "id": 3}
            //stm.setString(5, MPA_ID);
            final Set<Genre> genreSet = film.getGenre();
            //  "genres": [{ "id": 1}]
            //stm.setLong(6, genreSet);
            /*if (MPA_ID == null) {
                stm.setNull(5);
            } else {
                stm.setDate(4, Date.valueOf(birthday));
            }*/
            return stm;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void deleteFilm(Film film) {
    }

    @Override
    public void updateFilm(Film film) { //todo: 1.5. PUT  .../films/{id} - обновление фильма
        //UPDATE FILMS t SET
        //t.NAME = 'Film Updated',
        //t.DESCRIPTION = 'New film update decription',
        //t.RELEASE_DATE = '1989-04-17',
        //t.DURATION = 190,
        //t.GENRE_ID = 2
        //WHERE t.FILM_ID = {id};
    }

    public Film findFilmById(long id) { //todo: 1.2. GET .../films/{id} - получение каждого фильма по их уникальному идентификатору
        //SELECT *
        //FROM FILMS
        //WHERE id = {id};
        return null;
    }

    public List<Film> getFilmsList() { //todo: 1.1. GET .../films - получение всех фильмов
        //SELECT *
        //FROM FILMS;
        return null;
    }

    public List<Film> getTenPopularFilmsOfLikes(long count) {
        //todo: 1.3. GET .../films/popular?count={count} - возвращает список из первых count фильмов по количеству лайков
        //```
        //SELECT
        //    FL.FILM_ID,
        //    COUNT(USER_ID) AS LIKES,
        //    F.FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, M.RATING_NAME, G.GENRE, RATE
        //FROM LIKES_LIST as FL
        //JOIN FILMS F on F.FILM_ID = FL.FILM_ID
        //JOIN GENRE G on G.GENRE_ID = F.GENRE_ID
        //JOIN MPA M on F.MPA_ID = M.MPA_ID
        //GROUP BY FL.FILM_ID
        //ORDER BY LIKES DESC
        //LIMIT 10;
        return null;
    }

    public void deleteLikeForFilm(long filmId, long userId) { //todo: 1.7. DELETE .../films/{id}/like/{userId} — пользователь удаляет лайк
        //DELETE FROM LIKES_LIST
        //WHERE FILM_ID = {id} AND USER_ID = {userId};
    }

    public void addLikeForFilm(long filmId, long userId) { //todo: 1.6. PUT  .../films/{id}/like/{userId} — пользователь ставит лайк фильму
        //INSERT INTO LIKES_LIST (FILM_ID, USER_ID)
        //VALUES ({id}, {userId});
    }

    public void getAllGenres() { // GET /genres
        // Пример возвращаемого значения
        // {
        //   “id”: 1,
        //   “name”: “Комедия”
        // }
    }

    public void getGenre(long id) { // GET /genres/{id}
        //  "genres": [{ "id": 1}]
    }

    public void getAllMPA() { // GET /mpa
        // Пример возвращаемого значения
        // {
        //   “id”: 1,
        //   “name”: “G”
        // }
    }

    public void getMPA(long id) { // GET /mpa/{id}
        //  "mpa": { "id": 3}
    }
}