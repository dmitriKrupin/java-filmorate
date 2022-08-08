package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

/**
 * Создайте интерфейсы FilmStorage и UserStorage, в которых будут определены методы добавления, удаления и модификации объектов.
 */

public interface UserStorage {
    void addUser(User user);

    void deleteUser(User user);

    void updateUser(User user);

    void addFriendsInFriendsList(Long userId, Long friendId);

    void deleteFriendFromFriendsList(Long userId, Long friendId);

    List<User> getCommonFriendsList(long id, long otherId);

    List<User> getUsersAll();

    List<User> getFriendsList(long id);

    User findUserById(Long id);
}