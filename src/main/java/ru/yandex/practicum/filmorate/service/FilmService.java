package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorageImpl;
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
    @Autowired
    FilmDbStorageImpl filmDbStorage;
    private final UserService userService;

    @Autowired
    public FilmService(UserService userService) {
        this.userService = userService;
    }

    public void addLikeForFilm(long filmId, long userId) {
        filmDbStorage.addLikeForFilm(filmId, userId);
    }

    public void deleteLikeForFilm(long filmId, long userId) {
        filmDbStorage.deleteLikeForFilm(filmId, userId);
    }

    public List<Film> getTenPopularFilmsOfLikes(long count) { //вывод популярных фильмов по количеству лайков или первых 10
        return filmDbStorage.getTenPopularFilmsOfLikes(count);
        /*List<Film> popularFilmsList = new ArrayList<>();
        List<Film> storageFilmsList = filmDbStorage.getFilmsList();
        if (count > storageFilmsList.size()) {
            count = storageFilmsList.size();
        }
        Collections.sort(storageFilmsList);
        for (int i = 0; i < count; i++) {
            popularFilmsList.add(storageFilmsList.get(i));
        }
        return popularFilmsList;*/
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