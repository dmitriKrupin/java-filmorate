package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.util.List;

/**
 * Создайте FilmService, который будет отвечать за операции с фильмами, — добавление и удаление лайка,
 * вывод 10 наиболее популярных фильмов по количеству лайков. Пусть пока каждый пользователь может поставить
 * лайк фильму только один раз.
 */

//Добавьте к ним аннотацию @Service — тогда к ним можно будет получить доступ из контроллера.
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("FilmDbStorageImpl") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLikeForFilm(long filmId, long userId) {
        filmStorage.addLikeForFilm(filmId, userId);
    }

    public void deleteLikeForFilm(long filmId, long userId) {
        filmStorage.deleteLikeForFilm(filmId, userId);
    }

    public List<Film> getTenPopularFilmsOfLikes(long count) {
        return filmStorage.getTenPopularFilmsOfLikes(count);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getFilmsList();
    }

    public Film createFilm(Film film) {
        filmStorage.addFilm(film);
        return film;
    }

    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }

    public Film findFilmById(long id) {
        return filmStorage.findFilmById(id);
    }

    public List<Genre> getAllGenres() { // GET /genres
        return filmStorage.getAllGenres();
    }

    public Genre getGenre(long id) { // GET /genres/{id}
        return filmStorage.getGenre(id);
    }

    public List<MPA> getAllMPA() { // GET /mpa
        return filmStorage.getAllMPA();
    }

    public MPA getMPA(long id) { // GET /mpa/{id}
        return filmStorage.getMPA(id);
    }
}