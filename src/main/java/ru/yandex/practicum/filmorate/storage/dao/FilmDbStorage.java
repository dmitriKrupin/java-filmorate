package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.inMemoryStorage.FilmStorage;

import java.util.List;

/**
 * Вам пригодятся созданные ранее интерфейсы UserStorage и FilmStorage.
 * Напишите для них новую имплементацию — например,
 * UserDbStorage и FilmDbStorage. Эти классы будут DAO — объектами доступа к данным.
 * <p>
 * Напишите в DAO соответствующие мапперы и методы,
 * позволяющие сохранять пользователей и фильмы в базу данных и получать их из неё.
 */

public interface FilmDbStorage extends FilmStorage {
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