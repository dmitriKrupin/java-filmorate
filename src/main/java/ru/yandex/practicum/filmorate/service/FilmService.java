package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.impl.FilmDbStorageImpl;
import ru.yandex.practicum.filmorate.impl.UserDbStorageImpl;
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
public class FilmService {
    @Autowired
    FilmDbStorageImpl filmDbStorage;
    private final UserService userService;

    //private final InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    //todo: поменять на получение данных из SQL вместо InMemoryFilmStorage
    @Autowired
    public FilmService(UserService userService) {
        this.userService = userService;
    }

    public void addLikeForFilm(long filmId, long userId) { //добавление лайка
        filmDbStorage.addLikeForFilm(filmId, userId);
        /*Optional<Film> film = filmDbStorage.findFilmById(filmId);
        Set<Long> likesList;
        if (film.isPresent()) {
            if (film.get().getLikesList() != null) {
                likesList = film.get().getLikesList();
            } else {
                likesList = new TreeSet<>();
            }
            likesList.add(userId);
            film.get().setLikesList(likesList);
        }*/
    }

    public void deleteLikeForFilm(long filmId, long userId) { //удаление лайка
        filmDbStorage.deleteLikeForFilm(filmId, userId);
        /*Optional<Film> film = filmDbStorage.findFilmById(filmId);
        if (film.isPresent()) {
            if (film.get().getLikesList() != null) {
                Set<Long> likeList = film.get().getLikesList();
                likeList.remove(userId);
                film.get().setLikesList(likeList);
            } else {
                throw new NotFoundException("Лайка от пользователя с id: " + userId + " не найдено!");
            }
        }*/
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
        /*Film film = new Film();
        for (Film entry : filmDbStorage.getFilmsList()) {
            if (entry.getId() == id) {
                film = entry;
            }
        }
        return film;*/
    }

    public void getAllGenres() { // GET /genres
        filmDbStorage.getAllGenres();
    }

    public void getGenre(long id) { // GET /genres/{id}
        filmDbStorage.getGenre(id);
    }

    public void getAllMPA() { // GET /mpa
        filmDbStorage.getAllMPA();
    }

    public void getMPA(long id) { // GET /mpa/{id}
        filmDbStorage.getMPA(id);
    }
}