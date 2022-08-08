package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//аннотация @SpringBootTest, которой помечается класс с тестами, говорит о том, что
// перед запуском этих тестов необходим запуск всего приложения;
@AutoConfigureTestDatabase
//по аннотации @AutoConfigureTestDatabase Spring понимает, что перед запуском теста
// необходимо сконфигурировать тестовую БД вместо основной;
@RequiredArgsConstructor(onConstructor_ = @Autowired)
//аргумент аннотации @RequiredArgsConstructor указывает, что конструктор, созданный
// с помощью библиотеки Lombok, сможет получать зависимости через механизм @Autowired.
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scriptTest.sql")
class UserDbStorageImplTest {
    private final UserDbStorageImpl userDbStorage;
    private final Set<Long> friendsIdList = new HashSet<>();
    private final User userOne = new User(1,
            "one@mail.ru",
            "one",
            "user",
            "1946-08-20",
            friendsIdList,
            true);

    private final User userTwo = new User(2,
            "two@email.ru",
            "two",
            "user",
            "1989-08-20",
            friendsIdList,
            true);

    @Test
    void addUser() {
        friendsIdList.add(userTwo.getId());
        userDbStorage.addUser(userOne);
        userDbStorage.addUser(userTwo);
        assertEquals(userOne, userDbStorage.findUserById(1L));
    }

    @Test
    void deleteUser() {
        userDbStorage.addUser(userOne);
        userDbStorage.deleteUser(userOne);
        assertTrue(userDbStorage.getUsersAll().isEmpty());
    }

    @Test
    void updateUser() {
        userDbStorage.addUser(userOne);
        userOne.setName("Update name");
        userDbStorage.updateUser(userOne);
        assertEquals(userOne, userDbStorage.findUserById(1L));
    }

    @Test
    void findUserById() {
        userDbStorage.addUser(userOne);
        assertEquals(userOne, userDbStorage.findUserById(1L));
    }

    @Test
    void getUsersAll() {
        userDbStorage.addUser(userOne);
        userDbStorage.addUser(userTwo);
        assertEquals(List.of(userOne, userTwo), userDbStorage.getUsersAll());
    }

    @Test
    void addFriendsInFriendsList() {
        userDbStorage.addUser(userOne);
        userDbStorage.addUser(userTwo);
        userDbStorage.addFriendsInFriendsList(userOne.getId(), userTwo.getId());
        Assertions.assertEquals(List.of(userOne), userDbStorage.getFriendsList(userOne.getId()));
    }

    @Test
    void deleteFriendFromFriendsList() {
    }

    @Test
    void getFriendsList() {
    }

    @Test
    void getCommonFriendsList() {
    }
}