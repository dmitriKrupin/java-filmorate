package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

/**
 * Создайте интерфейсы FilmStorage и UserStorage, в которых будут определены методы добавления, удаления и модификации объектов.
 */

public interface UserStorage {
    void addUser(User user);

    void deleteUser(User user);

    void updateUser(User user);
}
