package ru.yandex.practicum.filmorate.storage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Component
public class UserDbStorageImpl implements UserDbStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorageImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUser(User user) { //добавление пользователя из базы SQL
        final String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stm.setString(1, user.getEmail());
            stm.setString(2, user.getLogin());
            stm.setString(3, user.getName());
            final LocalDate birthday = LocalDate.parse(user.getBirthday());
            if (birthday == null) {
                stm.setNull(4, Types.DATE);
            } else {
                stm.setDate(4, Date.valueOf(birthday));
            }
            return stm;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void deleteUser(User user) { //удаление пользователя из базы SQL
        final String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, user.getId());
    }

    @Override
    public void updateUser(User user) { //2.6. PUT .../users/{id} - обновление пользователя
        final String sqlQuery = "UPDATE USERS AS U " +
                "SET U.EMAIL = ?, U.LOGIN = ?, U.NAME = ?, U.BIRTHDAY = ? " +
                "WHERE U.USER_ID = ?";
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQuery);
            stm.setString(1, user.getEmail());
            stm.setString(2, user.getLogin());
            stm.setString(3, user.getName());
            final LocalDate birthday = LocalDate.parse(user.getBirthday());
            if (birthday == null) {
                stm.setNull(4, Types.DATE);
            } else {
                stm.setDate(4, Date.valueOf(birthday));
            }
            stm.setLong(5, user.getId());
            return stm;
        });
    }

    @Override
    public User findUserById(Long userId) { //2.2. GET .../users/{id} - получение каждого пользователя по их уникальному идентификатору
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorageImpl::makeUser, userId);
        if (users.size() != 1) {
            throw new NotFoundException("Ошибка в методе findUserById, для id: " + userId);
        }
        return users.get(0);
    }

    private static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("USER_ID"));
        user.setEmail(rs.getString("EMAIL"));
        user.setLogin(rs.getString("LOGIN"));
        user.setName(rs.getString("NAME"));
        user.setBirthday(String.valueOf(rs.getDate("BIRTHDAY")));
        return user;
    }

    @Override
    public List<User> getUsersAll() { //2.1. GET .../users - получение списка всех пользователей
        final String sqlQuery = "SELECT * FROM USERS";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorageImpl::makeUser);
        return users;
    }

    public void addFriendsInFriendsList(Long userId, Long friendId) { //2.7. PUT .../users/{id}/friends/{friendId} — добавление в друзья
        if (!validateFriendInFriendsList(userId, friendId)) {
            final String sqlQueryUser = "INSERT INTO FRIENDS (USER_ID, FRIENDS_ID, STATUS_APPLICATION_FRIEND) " +
                    "VALUES (?, ?, ?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement stm = connection.prepareStatement(sqlQueryUser, new String[]{"USER_ID"});
                stm.setLong(1, userId);
                stm.setLong(2, friendId);
                stm.setBoolean(3, true);
                return stm;
            });
            final String sqlQueryFriend = "INSERT INTO FRIENDS (USER_ID, FRIENDS_ID, STATUS_APPLICATION_FRIEND) " +
                    "VALUES (?, ?, ?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement stm = connection.prepareStatement(sqlQueryFriend, new String[]{"USER_ID"});
                stm.setLong(1, friendId);
                stm.setLong(2, userId);
                stm.setBoolean(3, false);
                return stm;
            });
        } else {
            final String sqlQueryUser = "UPDATE FRIENDS AS FL " +
                    "SET FL.STATUS_APPLICATION_FRIEND = true " +
                    "WHERE FL.USER_ID = ? AND FRIENDS_ID = ?";
            jdbcTemplate.update(connection -> {
                PreparedStatement stm = connection.prepareStatement(sqlQueryUser);
                stm.setLong(1, friendId);
                stm.setLong(2, userId);
                return stm;
            });
        }
    }

    private boolean validateFriendInFriendsList(Long userId, Long friendId) {
        final String sqlQuery = "SELECT * FROM FRIENDS " +
                "WHERE USER_ID = ? AND FRIENDS_ID = ? AND STATUS_APPLICATION_FRIEND = false";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorageImpl::makeUser, userId, friendId);
        if (users.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteFriendFromFriendsList(Long userId, Long friendId) { //удаление пользователя из друзей
        final String sqlQueryUser = "UPDATE FRIENDS AS FL " +
                "SET FL.STATUS_APPLICATION_FRIEND = false " +
                "WHERE FL.USER_ID = ? AND FRIENDS_ID = ?";
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQueryUser);
            stm.setLong(1, userId);
            stm.setLong(2, friendId);
            return stm;
        });
    }

    @Override
    public List<User> getFriendsList(long id) { //2.3. GET .../users/{id}/friends — возвращаем список пользователей, являющихся его друзьями
        final String sqlQuery = "SELECT U.USER_ID, U.EMAIL, U.LOGIN, U.NAME, U.BIRTHDAY " +
                "FROM FRIENDS AS FL " +
                "JOIN USERS AS U on U.USER_ID = FL.FRIENDS_ID " +
                "WHERE FL.USER_ID = ? AND FL.STATUS_APPLICATION_FRIEND = true";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorageImpl::makeUser, id);
        if (users.size() < 1) {
            System.out.println("Пользователь с id: " + id + " одинок! :(");
        }
        return users;
    }

    public List<User> getCommonFriendsList(long id, long otherId) { //2.4. GET .../users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем
        final String sqlQuery = "SELECT * " +
                "FROM USERS " +
                "WHERE USER_ID IN ( " +
                "    SELECT FRIENDS_ID " +
                "    FROM FRIENDS " +
                "    WHERE FRIENDS.USER_ID = ?) " +
                "INTERSECT " +
                "SELECT * " +
                "FROM USERS " +
                "WHERE USER_ID IN ( " +
                "    SELECT FRIENDS_ID " +
                "    FROM FRIENDS " +
                "    WHERE FRIENDS.USER_ID = ?)";
        final List<User> commonFriendList = jdbcTemplate.query(sqlQuery, UserDbStorageImpl::makeUser, id, otherId);
        if (commonFriendList.size() < 1) {
            System.out.println("Для id " + id + " и " + " общих друзей нет:(");
        }
        return commonFriendList;
    }
}