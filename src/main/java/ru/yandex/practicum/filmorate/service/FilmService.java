package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

/**
 * Создайте FilmService, который будет отвечать за операции с фильмами, — добавление и удаление лайка,
 * вывод 10 наиболее популярных фильмов по количеству лайков. Пусть пока каждый пользователь может поставить
 * лайк фильму только один раз.
 */

//Добавьте к ним аннотацию @Service — тогда к ним можно будет получить доступ из контроллера.
@Service
public class FilmService {
    private final FilmDbStorage filmDbStorage;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    public void addLikeForFilm(long filmId, long userId) {
        filmDbStorage.addLikeForFilm(filmId, userId);
    }

    public void deleteLikeForFilm(long filmId, long userId) {
        filmDbStorage.deleteLikeForFilm(filmId, userId);
    }

    public List<Film> getTenPopularFilmsOfLikes(long count) {
        return filmDbStorage.getTenPopularFilmsOfLikes(count);
    }

    public List<Film> getAllFilms() {
        return filmDbStorage.getFilmsList();
    }

    public Film createFilm(Film film) {
        filmDbStorage.addFilm(film);
        return film;
    }

    public void updateFilm(Film film) {
        filmDbStorage.updateFilm(film);
    }

    public Film findFilmById(long id) {
        return filmDbStorage.findFilmById(id);
    }

    public List<Genre> getAllGenres() { // GET /genres
        return filmDbStorage.getAllGenres();
    }

    public Genre getGenre(long id) { // GET /genres/{id}
        return filmDbStorage.getGenre(id);
    }

    public List<MPA> getAllMPA() { // GET /mpa
        return filmDbStorage.getAllMPA();
    }

    public MPA getMPA(long id) { // GET /mpa/{id}
        return filmDbStorage.getMPA(id);
    }
}