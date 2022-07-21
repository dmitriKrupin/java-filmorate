package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

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

class UserDbStorageImplTest {

    /*
    @SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDBStorageTest {
    private final UserDbStorage userStorage;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Test
    @Order(1)
    void addUser() {
        User user = new User();
        user.setName("Mikhail");
        user.setEmail("test@gmail.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.parse("13.04.1986", formatter));
        userStorage.addUser(user);
        Optional<User> userOptional = userStorage.getUserById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "Mikhail")
                                .hasFieldOrPropertyWithValue("login", "login")
                                .hasFieldOrPropertyWithValue("email", "test@gmail.com")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.parse("13.04.1986", formatter))
                );
    }

    @Test
    @Order(2)
    void updateUser() {
        User user = userStorage.getUserById(1).orElse(new User());
        user.setLogin("Mikhail");
        userStorage.updateUser(user);

        Optional<User> userOptional = userStorage.getUserById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrPropertyWithValue("name", "Mikhail")
                                .hasFieldOrPropertyWithValue("login", "Mikhail")
                                .hasFieldOrPropertyWithValue("email", "test@gmail.com")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.parse("13.04.1986", formatter))
                );
    }
     */

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void findUserById() {
    }

    @Test
    void getUsersAll() {
    }

    @Test
    void addFriendsInFriendsList() {
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