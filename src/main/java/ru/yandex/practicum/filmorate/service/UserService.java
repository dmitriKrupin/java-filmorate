package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.util.List;

/**
 * Создайте UserService, который будет отвечать за такие операции с пользователями, как добавление в друзья,
 * удаление из друзей, вывод списка общих друзей. Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
 * То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
 */

//Добавьте к ним аннотацию @Service — тогда к ним можно будет получить доступ из контроллера.
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("UserDbStorageImpl") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriendsInFriendsList(Long userId, Long friendId) {
        userStorage.addFriendsInFriendsList(userId, friendId);
    }

    public void deleteFriendFromFriendsList(Long userId, Long friendId) {
        userStorage.deleteFriendFromFriendsList(userId, friendId);
    }

    public List<User> getCommonFriendsList(long id, long otherId) {
        return userStorage.getCommonFriendsList(id, otherId);
    }

    public List<User> getUsersAll() {
        return userStorage.getUsersAll();
    }

    public List<User> getFriendsList(long id) {
        return userStorage.getFriendsList(id);
    }

    public User findUserById(Long id) {
        return userStorage.findUserById(id);
    }

    public User createUser(User user) {
        userStorage.addUser(user);
        return user;
    }

    public void updateUser(User user) {
        userStorage.updateUser(user);
    }
}