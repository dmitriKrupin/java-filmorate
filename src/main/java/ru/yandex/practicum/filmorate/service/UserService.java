package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

/**
 * Создайте UserService, который будет отвечать за такие операции с пользователями, как добавление в друзья,
 * удаление из друзей, вывод списка общих друзей. Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
 * То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
 */

//Добавьте к ним аннотацию @Service — тогда к ним можно будет получить доступ из контроллера.
@Service
public class UserService {
    @Autowired
    UserDbStorageImpl userDbStorage;

    public void addFriendsInFriendsList(Long userId, Long friendId) {
        userDbStorage.addFriendsInFriendsList(userId, friendId);
    }

    public void deleteFriendFromFriendsList(Long userId, Long friendId) {
        userDbStorage.deleteFriendFromFriendsList(userId, friendId);
    }

    public List<User> getCommonFriendsList(long id, long otherId) {
        return userDbStorage.getCommonFriendsList(id, otherId);
    }

    public List<User> getUsersAll() {
        return userDbStorage.getUsersAll();
    }

    public List<User> getFriendsList(long id) {
        return userDbStorage.getFriendsList(id);
    }

    public User findUserById(Long id) {
        return userDbStorage.findUserById(id);
    }

    public User createUser(User user) {
        userDbStorage.addUser(user);
        return user;
    }

    public void updateUser(User user) {
        userDbStorage.updateUser(user);
    }
}