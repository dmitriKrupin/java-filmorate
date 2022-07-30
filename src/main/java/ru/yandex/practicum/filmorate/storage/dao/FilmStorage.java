package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

/**
 * Создайте интерфейсы FilmStorage и UserStorage, в которых будут определены методы добавления, удаления и модификации объектов.
 */

public interface FilmStorage {
    void addFilm(Film film);

    void deleteFilm(Film film);

    void updateFilm(Film film);

    void addLikeForFilm(long filmId, long userId);

    void deleteLikeForFilm(long filmId, long userId);

    List<Film> getTenPopularFilmsOfLikes(long count);

    List<Film> getFilmsList();

    Film findFilmById(long id);

    List<Genre> getAllGenres();

    Genre getGenre(long id);

    List<MPA> getAllMPA();

    MPA getMPA(long id);
}