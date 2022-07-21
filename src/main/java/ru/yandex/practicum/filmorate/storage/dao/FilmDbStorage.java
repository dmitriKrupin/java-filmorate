package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;

/**
 * Вам пригодятся созданные ранее интерфейсы UserStorage и FilmStorage.
 * Напишите для них новую имплементацию — например,
 * UserDbStorage и FilmDbStorage. Эти классы будут DAO — объектами доступа к данным.
 * <p>
 * Напишите в DAO соответствующие мапперы и методы,
 * позволяющие сохранять пользователей и фильмы в базу данных и получать их из неё.
 */

public interface FilmDbStorage {
    void addFilm(Film film);

    void deleteFilm(Film film);

    void updateFilm(Film film);
}