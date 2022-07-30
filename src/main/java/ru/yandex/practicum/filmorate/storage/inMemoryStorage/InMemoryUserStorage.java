package ru.yandex.practicum.filmorate.storage.inMemoryStorage;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.util.*;

/**
 * Создайте классы InMemoryFilmStorage и InMemoryUserStorage, имплементирующие новые интерфейсы,
 * и перенесите туда всю логику хранения, обновления и поиска объектов.
 */

//Добавьте к InMemoryFilmStorage и InMemoryUserStorage аннотацию @Component,
// чтобы впоследствии пользоваться внедрением зависимостей и передавать хранилища сервисам.
@Data
@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> userList = new HashMap<>(); // key: Long id, value: User user
    private static long userIdCounter = 1;

    @Override
    public void addUser(User user) {
        userList.put(user.getId(), user);
    }

    @Override
    public void deleteUser(User user) {
        for (Map.Entry<Long, User> users : userList.entrySet()) {
            if (users.getValue() == user) {
                userList.remove(user.getId());
            }
        }
    }

    @Override
    public void updateUser(User user) {
        for (int i = 0; i < getUsersAll().size(); i++) {
            if (getUsersAll().get(i).getId() == user.getId()) {
                userList.replace(user.getId(), user);
            }
        }
    }

    @Override
    public void addFriendsInFriendsList(Long userId, Long friendId) {
    }

    @Override
    public void deleteFriendFromFriendsList(Long userId, Long friendId) {
    }

    @Override
    public List<User> getCommonFriendsList(long id, long otherId) {
        return null;
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

    public List<User> getUsersAll() {
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
}