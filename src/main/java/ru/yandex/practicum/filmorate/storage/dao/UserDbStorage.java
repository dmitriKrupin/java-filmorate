package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

/**
 * Вам пригодятся созданные ранее интерфейсы UserStorage и FilmStorage.
 * Напишите для них новую имплементацию — например,
 * UserDbStorage и FilmDbStorage. Эти классы будут DAO — объектами доступа к данным.
 * <p>
 * Напишите в DAO соответствующие мапперы и методы,
 * позволяющие сохранять пользователей и фильмы в базу данных и получать их из неё.
 */

public interface UserDbStorage {
    void addUser(User user);

    void deleteUser(User user);

    void updateUser(User user);

    User findUserById(Long userId);

    List<User> getUsersAll();

    List<User> getFriendsList(long id);
}