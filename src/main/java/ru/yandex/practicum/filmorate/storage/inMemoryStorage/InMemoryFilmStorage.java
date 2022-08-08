package ru.yandex.practicum.filmorate.storage.inMemoryStorage;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * Создайте классы InMemoryFilmStorage и InMemoryUserStorage, имплементирующие новые интерфейсы,
 * и перенесите туда всю логику хранения, обновления и поиска объектов.
 */

//Добавьте к InMemoryFilmStorage и InMemoryUserStorage аннотацию @Component,
// чтобы впоследствии пользоваться внедрением зависимостей и передавать хранилища сервисам.

@Data
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final List<Film> filmsList = new ArrayList<>();
    private static long filmIdCounter = 1;

    @Override
    public void addFilm(Film film) {
        filmsList.add(film);
    }

    @Override
    public void deleteFilm(Film film) {
        filmsList.remove(film);
    }

    @Override
    public void updateFilm(Film film) {
        for (int i = 0; i < filmsList.size(); i++) {
            if (filmsList.get(i).getId() == film.getId()) {
                filmsList.set(i, film);
            }
        }
    }

    @Override
    public void addLikeForFilm(long filmId, long userId) {
    }

    @Override
    public void deleteLikeForFilm(long filmId, long userId) {
    }

    @Override
    public List<Film> getTenPopularFilmsOfLikes(long count) {
        return null;
    }

    public Long filmIdCounter() {
        return filmIdCounter++;
    }

    public void minusFilmIdCounter() {
        filmIdCounter--;
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

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

    @Override
    public Genre getGenre(long id) {
        return null;
    }

    @Override
    public List<MPA> getAllMPA() {
        return null;
    }

    @Override
    public MPA getMPA(long id) {
        return null;
    }
}