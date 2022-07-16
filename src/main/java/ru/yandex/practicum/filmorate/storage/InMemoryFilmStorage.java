package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

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
public class InMemoryFilmStorage implements FilmStorage { //todo: после проверки через @Qualifier можно удалить
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
}
