package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
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
    private final UserService userService;
    private final List<Film> filmsList = new ArrayList<>();
    private static long filmIdCounter = 1;

    @Autowired
    public FilmService(UserService userService) {
        this.userService = userService;
    }

    public void addLikeForFilm(long filmId, long userId) { //добавление лайка
        Film film = findFilmById(filmId);
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
        Film film = findFilmById(filmId);
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
        if (count > filmsList.size()) {
            count = filmsList.size();
        }
        Collections.sort(filmsList);
        for (int i = 0; i < count; i++) {
            popularFilmsList.add(filmsList.get(i));
        }
        return popularFilmsList;
    }

    public List<Film> filmsGetAll() {
        return filmsList;
    }

    public Film createFilm(Film film) {
        filmsList.add(film);
        return film;
    }

    public Long filmIdCounter() {
        return filmIdCounter++;
    }

    public Long minusFilmIdCounter() {
        return filmIdCounter--;
    }

    public Film findFilmById(long id) {
        Film film = new Film();
        for (Film entry : filmsList) {
            if (entry.getId() == id) {
                film = entry;
            }
        }
        return film;
    }
}
