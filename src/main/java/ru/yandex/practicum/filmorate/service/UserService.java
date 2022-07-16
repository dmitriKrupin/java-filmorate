package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.impl.UserDbStorageImpl;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.annotation.Priority;
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

    //private final InMemoryUserStorage userStorage = new InMemoryUserStorage();

    public void addFriendsInFriendsList(Long userId, Long friendId) { //добавление в друзья
        userDbStorage.addFriendsInFriendsList(userId, friendId);
        /*User user = userDbStorage.findUserById(userId);
        User friend = userDbStorage.findUserById(friendId);
        Set<Long> userFriendsList;
        if (user.getFriendsIdList() != null) {
            userFriendsList = user.getFriendsIdList();
        } else {
            userFriendsList = new TreeSet<>();
        }
        userFriendsList.add(friendId);
        user.setFriendsIdList(userFriendsList);

        Set<Long> friendFriendList;
        if (friend.getFriendsIdList() != null) {
            friendFriendList = friend.getFriendsIdList();
        } else {
            friendFriendList = new TreeSet<>();
        }
        friendFriendList.add(userId);
        friend.setFriendsIdList(friendFriendList);*/
    }

    public void deleteFriendFromFriendsList(Long userId, Long friendId) { //удаление из друзей
        userDbStorage.deleteFriendFromFriendsList(userId, friendId);
        /*User user = userDbStorage.findUserById(userId);
        User friend = userDbStorage.findUserById(friendId);
        Set<Long> userFriendsIdList = user.getFriendsIdList();
        userFriendsIdList.remove(friendId);
        user.setFriendsIdList(userFriendsIdList);
        Set<Long> friendFriendsList = friend.getFriendsIdList();
        friendFriendsList.remove(userId);
        user.setFriendsIdList(friendFriendsList);*/
    }

    public List<User> getCommonFriendsList(long id, long otherId) { //вывод списка общих друзей
        return userDbStorage.getCommonFriendsList(id, otherId);
        /*List<User> commonList = new ArrayList<>();
        User user = userDbStorage.findUserById(id);
        User otherUser = userDbStorage.findUserById(otherId);
        Set<Long> userFriendsIdList = user.getFriendsIdList();
        Set<Long> otherFriendsIdList = otherUser.getFriendsIdList();
        if (userFriendsIdList != null && otherFriendsIdList != null) {
            for (int i = 0; i < userFriendsIdList.size(); i++) {
                for (int j = i; j < otherFriendsIdList.size(); j++) {
                    if (userFriendsIdList.toArray()[i] == otherFriendsIdList.toArray()[j]) {
                        commonList.add(userDbStorage.findUserById((Long) userFriendsIdList.toArray()[i]));
                    }
                }
            }
        }
        return commonList;*/
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