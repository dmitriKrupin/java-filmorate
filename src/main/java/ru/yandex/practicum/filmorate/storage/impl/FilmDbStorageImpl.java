package ru.yandex.practicum.filmorate.storage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.*;
import java.util.*;

@Component
public class FilmDbStorageImpl implements FilmDbStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorageImpl.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFilm(Film film) { //1.4. POST .../films - добавление фильма
        //todo: добавить правильную реализацию добавления нескольких жанров фильму через таблицы GENRE и GENRE_LIST
        final String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID, GENRE_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        final long mpaId = film.getMpa().getId();
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stm.setString(1, film.getName());
            stm.setString(2, film.getDescription());
            stm.setString(3, film.getReleaseDate());
            stm.setLong(4, film.getDuration());
            stm.setLong(5, mpaId);
            if (film.getGenres() == null) {
                stm.setNull(6, Types.NULL);
            } else {
                for (Genre entry : film.getGenres()) {
                    stm.setLong(6, entry.getId());
                }
            }
            return stm;
        }, keyHolder);
        film.setMpa(setMpaForFilm(mpaId));
        film.setGenres(setGenreForFilm(film));
        film.setId(keyHolder.getKey().longValue());
    }

    private MPA setMpaForFilm(Long mpaId) {
        MPA mpa = getMPA(mpaId);
        MPA mpaForFilm = new MPA();
        mpaForFilm.setId(mpa.getId());
        mpaForFilm.setName(mpa.getName());
        return mpaForFilm;
    }

    private Set<Genre> setGenreForFilm(Film film) {
        if (film.getGenres() != null) {
            Set<Genre> genres = new HashSet<>();
            for (Genre entry : film.getGenres()) {
                Genre genre = getGenre(entry.getId());
                genres.add(genre);
            }
            return genres;
        } else {
            return null;
        }
    }

    @Override
    public void deleteFilm(Film film) {
        final String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQuery);
            stm.setLong(1, film.getId());
            return stm;
        });
    }

    @Override
    public void updateFilm(Film film) { //1.5. PUT  .../films/{id} - обновление фильма
        //todo: добавить правильную реализацию добавления нескольких жанров фильму через таблицы GENRE и GENRE_LIST
        final String sqlQuery = "UPDATE FILMS AS F " +
                "SET F.NAME = ?, F.DESCRIPTION = ?, F.RELEASE_DATE = ?, F.DURATION = ?, F.MPA_ID = ?, F.GENRE_ID = ? " +
                "WHERE F.FILM_ID = ?";
        final long mpaId = film.getMpa().getId();
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQuery);
            stm.setString(1, film.getName());
            stm.setString(2, film.getDescription());
            stm.setString(3, film.getReleaseDate());
            stm.setLong(4, film.getDuration());
            stm.setLong(5, mpaId);
            film.setMpa(setMpaForFilm(mpaId));
            if (film.getGenres() != null) {
                if (film.getGenres().size() == 0) {
                    stm.setNull(6, Types.NULL);
                } else {
                    for (Genre entry : film.getGenres()) {
                        stm.setLong(6, entry.getId());
                        film.setGenres(setGenreForFilm(film));
                    }
                }
            } else {
                stm.setNull(6, Types.NULL);
            }
            stm.setLong(7, film.getId());
            return stm;
        });
        //film.setMpa(setMpaForFilm(mpaId));
        //film.setGenres(setGenreForFilm(film));
    }

    public Film findFilmById(long id) { //1.2. GET .../films/{id} - получение каждого фильма по их уникальному идентификатору
        final String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, id);
        if (films.size() < 1) {
            throw new NotFoundException("Ошибка в методе findUserById, для id: " + id);
        }
        return films.get(0);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        //todo: добавить правильную реализацию добавления нескольких жанров фильму через таблицы GENRE и GENRE_LIST
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getString("RELEASE_DATE"));
        film.setDuration(rs.getLong("DURATION"));
        final long mpaId = rs.getLong("MPA_ID");
        film.setMpa(setMpaForFilm(mpaId));
        final long genreId = rs.getLong("GENRE_ID");
        final Set<Genre> genres = new HashSet<>();
        if (genreId == 0) {
            film.setGenres(null);
        } else {
            genres.add(getGenre(genreId));
            film.setGenres(genres);
        }
        return film;
    }

    public List<Film> getFilmsList() { //1.1. GET .../films - получение всех фильмов
        final String sqlQuery = "SELECT * FROM FILMS";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm);
        if (films.size() < 1) {
            System.out.println("Нет фильмов для выдачи. Добавьте фильмы в базу данных.");
        }
        return films;
    }

    public void addLikeForFilm(long filmId, long userId) { //1.6. PUT  .../films/{id}/like/{userId} — пользователь ставит лайк фильму
        final String sqlQuery = "INSERT INTO LIKES_LIST (FILM_ID, USER_ID) " +
                "VALUES (?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stm.setLong(1, filmId);
            stm.setLong(2, userId);
            return stm;
        }, keyHolder);
    }

    public void deleteLikeForFilm(long filmId, long userId) { //1.7. DELETE .../films/{id}/like/{userId} — пользователь удаляет лайк
        if (filmId > 0 && userId > 0) {
            final String sqlQuery = "DELETE FROM LIKES_LIST WHERE FILM_ID = ? AND USER_ID = ?";
            jdbcTemplate.update(connection -> {
                PreparedStatement stm = connection.prepareStatement(sqlQuery);
                stm.setLong(1, filmId);
                stm.setLong(2, userId);
                return stm;
            });
        } else {
            throw new NotFoundException("Проверьте правильность ввода, id фильма: " + filmId +
                    ", id пользователя: " + userId);
        }
    }

    public List<Film> getTenPopularFilmsOfLikes(long count) {//1.3. GET .../films/popular?count={count} - возвращает список из первых count фильмов по количеству лайков
        //todo: проверить правильность вывода метода
        //todo: добавить правильную реализацию добавления нескольких жанров фильму через таблицы GENRE и GENRE_LIST
        final String sqlQuery = "SELECT FL.FILM_ID, " +
                "F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.MPA_ID, G.GENRE_ID  " +
                "FROM LIKES_LIST AS FL " +
                "JOIN FILMS AS F ON F.FILM_ID = FL.FILM_ID " +
                "JOIN MPA M on M.MPA_ID = F.MPA_ID " +
                "JOIN GENRE G on G.GENRE_ID = F.GENRE_ID " +
                "GROUP BY FL.FILM_ID " +
                "ORDER BY COUNT(FL.USER_ID) DESC " +
                "LIMIT ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, count);
        if (films.size() < 1) {
            System.out.println("Нет фильмов для выдачи. Поставьте лайки фильмам и повторите попытку.");
        }
        return films;
    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getLong("GENRE_ID"));
        genre.setName(resultSet.getString("GENRE"));
        return genre;
    }

    public Genre getGenre(long id) { // GET /genres/{id}
        final String sqlQuery = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        final List<Genre> genre = jdbcTemplate.query(sqlQuery, this::makeGenre, id);
        if (genre.size() < 1) {
            throw new NotFoundException("Ошибка в методе findUserById, для id: " + id);
        }
        return genre.get(0);
    }

    public List<Genre> getAllGenres() { // GET /genres
        final String sqlQuery = "SELECT * FROM GENRE";
        final List<Genre> genreList = jdbcTemplate.query(sqlQuery, this::makeGenre);
        if (genreList.size() < 6) {
            throw new NotFoundException("Ошибка в методе getAllGenres!");
        }
        Collections.sort(genreList);
        return genreList;
    }

    private MPA makeMPA(ResultSet resultSet, int rowNum) throws SQLException {
        MPA mpa = new MPA();
        mpa.setId(resultSet.getLong("MPA_ID"));
        mpa.setName(resultSet.getString("RATING_NAME"));
        return mpa;
    }

    public List<MPA> getAllMPA() { // GET /mpa
        final String sqlQuery = "SELECT * FROM MPA";
        final List<MPA> mpaList = jdbcTemplate.query(sqlQuery, this::makeMPA);
        if (mpaList.size() < 5) {
            throw new NotFoundException("Ошибка в методе getAllMPA!");
        }
        return mpaList;
    }

    public MPA getMPA(long id) { // GET /mpa/{id}
        final String sqlQuery = "SELECT * FROM MPA WHERE MPA_ID = ?";
        final List<MPA> mpaList = jdbcTemplate.query(sqlQuery, this::makeMPA, id);
        if (mpaList.size() < 1) {
            throw new NotFoundException("Ошибка в методе findUserById, для id: " + id);
        }
        return mpaList.get(0);
    }
}