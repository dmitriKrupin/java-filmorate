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
        final String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        final long mpaId = film.getMpa().getId();
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stm.setString(1, film.getName());
            stm.setString(2, film.getDescription());
            stm.setString(3, film.getReleaseDate());
            stm.setLong(4, film.getDuration());
            stm.setLong(5, mpaId);
            return stm;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        film.setMpa(setMpaForFilm(mpaId));
        if (film.getGenres() != null) {
            film.setGenres(setGenreForFilm(film));
            addGenreInGenreList(film, film.getGenres());
        }
    }

    @Override
    public void updateFilm(Film film) { //1.5. PUT  .../films/{id} - обновление фильма
        final String sqlQuery = "UPDATE FILMS AS F " +
                "SET F.NAME = ?, F.DESCRIPTION = ?, F.RELEASE_DATE = ?, F.DURATION = ?, F.MPA_ID = ? " +
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
            stm.setLong(6, film.getId());
            return stm;
        });
        if (film.getGenres() != null) {
            if (film.getGenres().size() != 0) {
                film.setGenres(setGenreForFilm(film));
                updateGenreInGenreList(film);
            } else {
                film.setGenres(setGenreForFilm(film));
                deleteGenreInGenreList(film);
            }
        }
    }

    private void addGenreInGenreList(Film film, Set<Genre> genres) {
        for (Genre genre : genres) {
            final String sqlQuery = "INSERT INTO GENRE_LIST (GENRE_ID, FILM_ID) " +
                    "VALUES (?, ?)";
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stm = connection.prepareStatement(sqlQuery, new String[]{"GENRE_ID"});
                    stm.setLong(1, genre.getId());
                    stm.setLong(2, film.getId());
                return stm;
            }, keyHolder);
        }
        film.setGenres(genres);
    }

    private void updateGenreInGenreList(Film film) {
        final Set<Genre> currentGenreList = findGenreListByFilmId(film.getId());
        final Film currentFilm = findFilmById(film.getId());
        final Set<Genre> updateGenreList = film.getGenres();
        final List<Genre> sortedGenreList = new ArrayList<>(updateGenreList);
        Collections.sort(sortedGenreList);
        final Set<Genre> sortedGenreSet = new HashSet<>(sortedGenreList);

        if (currentGenreList == null || currentGenreList.size() <= updateGenreList.size()) {
            for (Genre genre : sortedGenreSet) {
                final String sqlQuery = "MERGE INTO GENRE_LIST VALUES ( ?, ? )";
                jdbcTemplate.update(connection -> {
                    PreparedStatement stm = connection.prepareStatement(sqlQuery);
                    stm.setLong(1, genre.getId());
                    stm.setLong(2, film.getId());
                    return stm;
                });
            }
        }
        if (currentGenreList != null && currentGenreList.size() > updateGenreList.size()) {
            deleteGenreInGenreList(currentFilm);
            addGenreInGenreList(film, sortedGenreSet);
        }
    }

    private void deleteGenreInGenreList(Film film) {
        final String sqlQuery = "DELETE FROM GENRE_LIST WHERE FILM_ID = ? ";
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQuery);
            stm.setLong(1, film.getId());
            return stm;
        });
    }

    private Set<Genre> findGenreListByFilmId(long filmId) {
        final String sqlQuery = "SELECT GL.GENRE_ID, GENRE " +
                "FROM GENRE_LIST AS GL " +
                "JOIN GENRE AS G ON GL.GENRE_ID = G.GENRE_ID " +
                "WHERE GL.FILM_ID = ?";
        final Set<Genre> genreSet = new HashSet<>();
        final List<Genre> genreList = jdbcTemplate.query(sqlQuery, this::makeGenre, filmId);
        if (genreList.size() == 0) {
            return null;
        } else {
            for (Genre genre : genreList) {
                genreSet.add(genre);
            }
            if (genreSet.size() < 1) {
                throw new NotFoundException("Ошибка в методе findGenreByFilmId, для id: " + filmId);
            }
        }
        return genreSet;
    }

    private MPA setMpaForFilm(Long mpaId) {
        MPA mpa = getMPA(mpaId);
        MPA mpaForFilm = new MPA();
        mpaForFilm.setId(mpa.getId());
        mpaForFilm.setName(mpa.getName());
        return mpaForFilm;
    }

    private Set<Genre> setGenreForFilm(Film film) {
        Set<Genre> genres = new HashSet<>();
        if (film.getGenres() != null) {
            for (Genre entry : film.getGenres()) {
                Genre genre = getGenre(entry.getId());
                genres.add(genre);
            }
        } else {
            return null;
        }
        return genres;
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
    public Film findFilmById(long id) { //1.2. GET .../films/{id} - получение каждого фильма по их уникальному идентификатору
        final String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, id);
        if (films.size() < 1) {
            throw new NotFoundException("Ошибка в методе findUserById, для id: " + id);
        }
        return films.get(0);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getString("RELEASE_DATE"));
        film.setDuration(rs.getLong("DURATION"));
        final long mpaId = rs.getLong("MPA_ID");
        film.setMpa(setMpaForFilm(mpaId));
        final Set<Genre> genres = findGenreListByFilmId(film.getId());
        film.setGenres(genres);
        return film;
    }

    @Override
    public List<Film> getFilmsList() { //1.1. GET .../films - получение всех фильмов
        final String sqlQuery = "SELECT * FROM FILMS";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm);
        if (films.size() < 1) {
            System.out.println("Нет фильмов для выдачи. Добавьте фильмы в базу данных.");
        }
        return films;
    }

    @Override
    public void addLikeForFilm(long filmId, long userId) { //1.6. PUT  .../films/{id}/like/{userId} — пользователь ставит лайк фильму
        final String sqlQuery = "INSERT INTO LIKES (FILM_ID, USER_ID) " +
                "VALUES (?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stm.setLong(1, filmId);
            stm.setLong(2, userId);
            return stm;
        }, keyHolder);
    }

    @Override
    public void deleteLikeForFilm(long filmId, long userId) { //1.7. DELETE .../films/{id}/like/{userId} — пользователь удаляет лайк
        if (filmId > 0 && userId > 0) {
            final String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
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

    @Override
    public List<Film> getTenPopularFilmsOfLikes(long count) {//1.3. GET .../films/popular?count={count} - возвращает список из первых count фильмов по количеству лайков
        final String sqlQuery = "SELECT F.FILM_ID, " +
                "F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.MPA_ID " +
                "FROM LIKES AS FL " +
                "JOIN FILMS AS F ON F.FILM_ID = FL.FILM_ID " +
                "GROUP BY FL.FILM_ID " +
                "ORDER BY COUNT(FL.USER_ID) DESC " +
                "LIMIT ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, count);
        if (films.size() < 1) {
            return getFilmsList();
        } else {
            return films;
        }
    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getLong("GENRE_ID"));
        genre.setName(resultSet.getString("GENRE"));
        return genre;
    }

    @Override
    public Genre getGenre(long id) { // GET /genres/{id}
        final String sqlQuery = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        final List<Genre> genre = jdbcTemplate.query(sqlQuery, this::makeGenre, id);
        if (genre.size() < 1) {
            throw new NotFoundException("Ошибка в методе findUserById, для id: " + id);
        }
        return genre.get(0);
    }

    @Override
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

    @Override
    public List<MPA> getAllMPA() { // GET /mpa
        final String sqlQuery = "SELECT * FROM MPA";
        final List<MPA> mpaList = jdbcTemplate.query(sqlQuery, this::makeMPA);
        if (mpaList.size() < 5) {
            throw new NotFoundException("Ошибка в методе getAllMPA!");
        }
        return mpaList;
    }

    @Override
    public MPA getMPA(long id) { // GET /mpa/{id}
        final String sqlQuery = "SELECT * FROM MPA WHERE MPA_ID = ?";
        final List<MPA> mpaList = jdbcTemplate.query(sqlQuery, this::makeMPA, id);
        if (mpaList.size() < 1) {
            throw new NotFoundException("Ошибка в методе findUserById, для id: " + id);
        }
        return mpaList.get(0);
    }
}