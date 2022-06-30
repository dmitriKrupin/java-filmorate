package ru.yandex.practicum.filmorate.storage;

/**
 * Создайте интерфейсы FilmStorage и UserStorage, в которых будут определены методы добавления, удаления и модификации объектов.
 */

public interface FilmStorage {
    public void addFilm();

    public void deleteFilm();

    public void updateFilm();
}
