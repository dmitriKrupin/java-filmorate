package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;

/**
 * todo: Создайте классы InMemoryFilmStorage и InMemoryUserStorage, имплементирующие новые интерфейсы,
 * и перенесите туда всю логику хранения, обновления и поиска объектов.
 */

//Добавьте к InMemoryFilmStorage и InMemoryUserStorage аннотацию @Component,
// чтобы впоследствии пользоваться внедрением зависимостей и передавать хранилища сервисам.
@Component
public class InMemoryFilmStorage implements FilmStorage {
    @Override
    public void addFilm() {

    }

    @Override
    public void deleteFilm() {

    }

    @Override
    public void updateFilm() {

    }
}
