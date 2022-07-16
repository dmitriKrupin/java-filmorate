package ru.yandex.practicum.filmorate.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
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
    public void deleteUser(User user) { //todo: удаление пользователя из базы SQL
        final String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorageImpl::makeUser, user.getId());
        if (users.size() != 1) {
            throw new NotFoundException("Ошибка в методе deleteUser для ползователя с id: " + user.getId());
        }
    }

    public void deleteFriendFromFriendsList(Long userId, Long friendId) { //удаление пользователя из друзей
        final String sqlQuery = "DELETE FROM FRIENDS_ID_LIST " +
                "WHERE USER_ID = ?" + //{id}
                "AND FRIENDS_ID = ?" + //{friendId}
                "AND STATUS_APPLICATION_FRIEND = true";
        //final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQuery);
            stm.setLong(1, userId);
            stm.setLong(2, friendId);
            return stm;
        }/*, keyHolder*/);
    }

    @Override
    public void updateUser(User user) { //2.6. PUT .../users/{id} - обновление пользователя
        final String sqlQuery = "UPDATE USERS AS U " +
                "SET U.EMAIL = ?, U.LOGIN = ?, U.NAME = ?, U.BIRTHDAY = ? " +
                "WHERE U.USER_ID = ?";
        //final KeyHolder keyHolder = new GeneratedKeyHolder();
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
        }/*, keyHolder*/);
        user.setId(user.getId());
    }

    public void addFriendsInFriendsList(Long userId, Long friendId) { //2.7. PUT .../users/{id}/friends/{friendId} — добавление в друзья
        final String sqlQuery = "INSERT INTO FRIENDS_ID_LIST (USER_ID, FRIENDS_ID, STATUS_APPLICATION_FRIEND) " +
                "VALUES (?, ?, ?)"; //VALUES ({id}, {friendId}, true);
        //final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stm.setLong(1, userId);
            stm.setLong(2, friendId);
            //И последнее небольше изменение: дружба должна стать односторонней.
            // Это значит, что если какой-то пользователь оставил вам заявку в друзья,
            // то он будет в списке ваших друзей, а вы в его — нет.
            final boolean statusApplicationFriend;
            //проверять через длину списка дружбы, если другу 1 добавился в друзья друг 2,
            // то список друзей друга 1 будет 1, а список друзей друга 2 может быть 0 или также 1
            stm.setBoolean(3, true); //todo: написать алгоритм выбора true или false
            return stm;
        }/*, keyHolder*/);
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
        if (users.size() != 1) {
            throw new NotFoundException("Ошибка в методе getUsersAll!");
        }
        return users;
    }

    @Override
    public List<User> getFriendsList(long id) { //todo: 2.3. GET .../users/{id}/friends — возвращаем список пользователей, являющихся его друзьями
        //```
        //SELECT *
        //FROM FRIENDS_ID_LIST
        //WHERE USER_ID = {id};
        return null;
    }

    public List<User> getCommonFriendsList(long id, long otherId) { //todo: 2.4. GET .../users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем
        //```
        //SELECT *
        //FROM USERS
        //WHERE USER_ID IN (
        //    SELECT FRIENDS_ID
        //    FROM FRIENDS_ID_LIST
        //    WHERE FRIENDS_ID_LIST.USER_ID = {id})
        //INTERSECT
        //SELECT *
        //FROM USERS
        //WHERE USER_ID IN (
        //    SELECT FRIENDS_ID
        //    FROM FRIENDS_ID_LIST
        //    WHERE FRIENDS_ID_LIST.USER_ID = {otherId});
        return null;
    }
}