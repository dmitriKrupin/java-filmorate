package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Создайте UserService, который будет отвечать за такие операции с пользователями, как добавление в друзья,
 * удаление из друзей, вывод списка общих друзей. Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
 * То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
 */

//Добавьте к ним аннотацию @Service — тогда к ним можно будет получить доступ из контроллера.
@Service
public class UserService extends InMemoryUserStorage {
    private final InMemoryUserStorage userStorage = new InMemoryUserStorage();

    public void addFriendsInFriendsList(Long userId, Long friendId) { //добавление в друзья
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);

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
        friend.setFriendsIdList(friendFriendList);
    }

    public void deleteFriendFromFriendsList(Long userId, Long friendId) { //удаление из друзей
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);

        Set<Long> userFriendsIdList = user.getFriendsIdList();
        userFriendsIdList.remove(friendId);
        user.setFriendsIdList(userFriendsIdList);

        Set<Long> friendFriendsList = friend.getFriendsIdList();
        friendFriendsList.remove(userId);
        user.setFriendsIdList(friendFriendsList);
    }

    public List<User> getCommonFriendsList(long id, long otherId) { //вывод списка общих друзей
        List<User> commonList = new ArrayList<>();
        User user = userStorage.findUserById(id);
        User otherUser = userStorage.findUserById(otherId);
        Set<Long> userFriendsIdList = user.getFriendsIdList();
        Set<Long> otherFriendsIdList = otherUser.getFriendsIdList();
        if (userFriendsIdList != null && otherFriendsIdList != null) {
            for (int i = 0; i < userFriendsIdList.size(); i++) {
                for (int j = i; j < otherFriendsIdList.size(); j++) {
                    if (userFriendsIdList.toArray()[i] == otherFriendsIdList.toArray()[j]) {
                        commonList.add(userStorage.findUserById((Long) userFriendsIdList.toArray()[i]));
                    }
                }
            }
        }
        return commonList;
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
