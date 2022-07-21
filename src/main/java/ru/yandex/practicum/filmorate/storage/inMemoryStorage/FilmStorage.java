package ru.yandex.practicum.filmorate.storage.inMemoryStorage;

import ru.yandex.practicum.filmorate.model.Film;

/**
 * Создайте интерфейсы FilmStorage и UserStorage, в которых будут определены методы добавления, удаления и модификации объектов.
 */

public interface FilmStorage { //todo: можно удалить
    void addFilm(Film film);

    void deleteFilm(Film film);

    void updateFilm(Film film);
}
