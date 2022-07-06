package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;

/**
 * Создайте FilmService, который будет отвечать за операции с фильмами, — добавление и удаление лайка,
 * вывод 10 наиболее популярных фильмов по количеству лайков. Пусть пока каждый пользователь может поставить
 * лайк фильму только один раз.
 */

//Добавьте к ним аннотацию @Service — тогда к ним можно будет получить доступ из контроллера.
@Service
public class FilmService extends InMemoryFilmStorage {
    private final UserService userService;
    private final InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();

    @Autowired
    public FilmService(UserService userService) {
        this.userService = userService;
    }

    public void addLikeForFilm(long filmId, long userId) { //добавление лайка
        Film film = filmStorage.findFilmById(filmId);
        Set<Long> likesList;
        if (film.getLikesList() != null) {
            likesList = film.getLikesList();
        } else {
            likesList = new TreeSet<>();
        }
        likesList.add(userId);
        film.setLikesList(likesList);
    }

    public void deleteLikeForFilm(long filmId, long userId) { //удаление лайка
        Film film = filmStorage.findFilmById(filmId);
        if (film.getLikesList() != null) {
            Set<Long> likeList = film.getLikesList();
            likeList.remove(userId);
            film.setLikesList(likeList);
        } else {
            throw new NotFoundException("Лайка от пользователя с id: " + userId + " не найдено!");
        }
    }

    public List<Film> getTenPopularFilmsOfLikes(long count) { //вывод популярных фильмов по количеству лайков или первых 10
        List<Film> popularFilmsList = new ArrayList<>();
        List<Film> storageFilmsList = filmStorage.getFilmsList();
        if (count > storageFilmsList.size() /*filmsList.size()*/) {
            count = storageFilmsList.size();
        }
        Collections.sort(storageFilmsList);
        for (int i = 0; i < count; i++) {
            popularFilmsList.add(storageFilmsList.get(i));
        }
        return popularFilmsList;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getFilmsList();
        //return filmsList;
    }

    public Film createFilm(Film film) {
        filmStorage.addFilm(film);
        //filmsList.add(film);
        return film;
    }

    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }
}
