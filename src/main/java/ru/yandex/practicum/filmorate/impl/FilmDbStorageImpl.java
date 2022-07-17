package ru.yandex.practicum.filmorate.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.guide.Genre;
import ru.yandex.practicum.filmorate.guide.MPA;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.*;
import java.util.*;

@Component
public class FilmDbStorageImpl implements FilmDbStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorageImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFilm(Film film) { //1.4. POST .../films - добавление фильма
        //INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID, GENRE_ID, RATE)
        //VALUES ('New film', 'New film about friends', '1999-04-30', 120, 2, 3, 4);
        final String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stm.setString(1, film.getName());
            stm.setString(2, film.getDescription());
            stm.setString(3, film.getReleaseDate());
            stm.setLong(4, film.getDuration());
            stm.setLong(5, film.getMpa().getId());
            return stm;
        }, keyHolder);
        film.setGenres(genreSetForFilm(film));
        film.setId(keyHolder.getKey().longValue());
    }

    private Set<Genre> genreSetForFilm(Film film) {
        return film.getGenres();
    }

    @Override
    public void deleteFilm(Film film) {
    }

    @Override
    public void updateFilm(Film film) { //1.5. PUT  .../films/{id} - обновление фильма
        final String sqlQuery = "UPDATE FILMS AS F " +
                "SET F.NAME = ?, F.DESCRIPTION = ?, F.RELEASE_DATE = ?, F.DURATION = ?, F.MPA_ID = ?" +
                "WHERE F.FILM_ID = ?";
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQuery);
            stm.setString(1, film.getName());
            stm.setString(2, film.getDescription());
            stm.setString(3, film.getReleaseDate());
            stm.setLong(4, film.getDuration());
            stm.setLong(5, film.getMpa().getId());
            stm.setLong(6, film.getId());
            return stm;
        });
        film.setId(film.getId());
    }

    public Film findFilmById(long id) { //1.2. GET .../films/{id} - получение каждого фильма по их уникальному идентификатору
        final String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorageImpl::makeFilm, id);
        if (films.size() != 1) {
            throw new NotFoundException("Ошибка в методе findUserById, для id: " + id);
        }
        return films.get(0);
    }

    private static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getString("RELEASE_DATE"));
        film.setDuration(rs.getLong("DURATION"));
        final long mpaId = rs.getLong("MPA_ID");
        final MPA mpa = getMPA(mpaId);
        film.setMpa(mpa);
        final long genreId = rs.getLong("GENRE_ID");
        final Set<Genre> genre = getGenre(genreId);
        film.setGenres(genre);
        return film;
    }

    public List<Film> getFilmsList() { //1.1. GET .../films - получение всех фильмов
        final String sqlQuery = "SELECT * FROM FILMS";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorageImpl::makeFilm);
        if (films.size() != 1) {
            throw new NotFoundException("Ошибка в методе getFilmsList!");
        }
        return films;
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

    public List<Genre> getAllGenres() { // GET /genres
        final String sqlQuery = "SELECT * FROM GENRE";
        final List<Genre> genreList = jdbcTemplate.query(sqlQuery, FilmDbStorageImpl::makeGenre);
        if (genreList.size() < 6) {
            throw new NotFoundException("Ошибка в методе getAllGenres!");
        }
        Collections.sort(genreList);
        return genreList;
    }

    private static Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getLong("GENRE_ID"));
        genre.setName(resultSet.getString("GENRE"));
        return genre;
    }

    public static Set<Genre> getGenre(long id) { // GET /genres/{id}
        //  "genres": [{ "id": 1}]
        return null;
    }

    public List<MPA> getAllMPA() { // GET /mpa
        final String sqlQuery = "SELECT * FROM MPA";
        final List<MPA> mpaList = jdbcTemplate.query(sqlQuery, FilmDbStorageImpl::makeMPA);
        if (mpaList.size() < 5) {
            throw new NotFoundException("Ошибка в методе getAllMPA!");
        }
        return mpaList;
    }

    private static MPA makeMPA(ResultSet resultSet, int rowNum) throws SQLException {
        MPA mpa = new MPA();
        mpa.setId(resultSet.getLong("MPA_ID"));
        mpa.setName(resultSet.getString("RATING_NAME"));
        return mpa;
    }

    public static MPA getMPA(long id) { // GET /mpa/{id}
        //  "mpa": { "id": 3}
        return null;
    }
}