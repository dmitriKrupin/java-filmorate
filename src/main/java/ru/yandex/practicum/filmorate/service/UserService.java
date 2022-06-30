package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
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
    private final HashMap<Long, User> userList = new HashMap<>(); // key: Long id, value: User user
    private static long userIdCounter = 1;

    public void addFriendsInFriendsList(Long userId, Long friendId) { //добавление в друзья
        User user = findUserById(userId);
        User friend = findUserById(friendId);

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
        User user = findUserById(userId);
        User friend = findUserById(friendId);

        Set<Long> userFriendsIdList = user.getFriendsIdList();
        userFriendsIdList.remove(friendId);
        user.setFriendsIdList(userFriendsIdList);

        Set<Long> friendFriendsList = friend.getFriendsIdList();
        friendFriendsList.remove(userId);
        user.setFriendsIdList(friendFriendsList);
    }

    public List<User> getCommonFriendsList(long id, long otherId) { //вывод списка общих друзей
        List<User> commonList = new ArrayList<>();
        User user = findUserById(id);
        User otherUser = findUserById(otherId);
        Set<Long> userFriendsIdList = user.getFriendsIdList();
        Set<Long> otherFriendsIdList = otherUser.getFriendsIdList();
        if (userFriendsIdList != null && otherFriendsIdList != null) {
            for (int i = 0; i < userFriendsIdList.size(); i++) {
                for (int j = i; j < otherFriendsIdList.size(); j++) {
                    if (userFriendsIdList.toArray()[i] == otherFriendsIdList.toArray()[j]) {
                        commonList.add(findUserById((Long) userFriendsIdList.toArray()[i]));
                    }
                }
            }
        }
        return commonList;
    }

    public List<User> usersGetAll() {
        List<User> allUsers = new ArrayList<>();
        for (Map.Entry<Long, User> users : userList.entrySet()) {
            allUsers.add(users.getValue());
        }
        return allUsers;
    }

    public List<User> getFriendsList(long id) {
        User user = findUserById(id);
        Set<Long> friendsIdList = user.getFriendsIdList();
        List<User> friendsList = new ArrayList<>();
        for (Long entry : friendsIdList) {
            friendsList.add(findUserById(entry));
        }
        return friendsList;
    }

    public User createUser(User user) {
        userList.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        userList.replace(user.getId(), user);
        return user;
    }

    public Long userIdCounter() {
        return userIdCounter++;
    }

    public void minusUserIdCounter() {
        userIdCounter--;
    }

    public User findUserById(Long id) {
        User user = new User();
        for (Map.Entry<Long, User> entry : userList.entrySet()) {
            if (entry.getKey().equals(id)) {
                user = entry.getValue();
            }
        }
        return user;
    }
}
