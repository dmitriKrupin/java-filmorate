package ru.yandex.practicum.filmorate.storage;

/**
 * Создайте интерфейсы FilmStorage и UserStorage, в которых будут определены методы добавления, удаления и модификации объектов.
 */

public interface UserStorage {
    public void addUser();

    public void deleteUser();

    public void updateUser();
}
