package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inMemoryStorage.UserStorage;

import java.util.List;

/**
 * Вам пригодятся созданные ранее интерфейсы UserStorage и FilmStorage.
 * Напишите для них новую имплементацию — например,
 * UserDbStorage и FilmDbStorage. Эти классы будут DAO — объектами доступа к данным.
 * <p>
 * Напишите в DAO соответствующие мапперы и методы,
 * позволяющие сохранять пользователей и фильмы в базу данных и получать их из неё.
 */

public interface UserDbStorage extends UserStorage {
    User findUserById(Long userId);

    List<User> getUsersAll();

    List<User> getFriendsList(long id);

    List<User> getCommonFriendsList(long id, long otherId);

    void deleteFriendFromFriendsList(Long userId, Long friendId);

    void addFriendsInFriendsList(Long userId, Long friendId);
}