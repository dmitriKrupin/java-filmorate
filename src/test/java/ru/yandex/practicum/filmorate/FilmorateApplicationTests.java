package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.impl.UserDbStorageImpl;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
//аннотация @SpringBootTest, которой помечается класс с тестами, говорит о том, что
// перед запуском этих тестов необходим запуск всего приложения;
@AutoConfigureTestDatabase
//по аннотации @AutoConfigureTestDatabase Spring понимает, что перед запуском теста
// необходимо сконфигурировать тестовую БД вместо основной;
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//аргумент аннотации @RequiredArgsConstructor указывает, что конструктор, созданный
// с помощью библиотеки Lombok, сможет получать зависимости через механизм @Autowired.

class FilmorateApplicationTests {
    //private final UserDbStorageImpl userStorage;

   // @Test
    /*public void testFindUserById() {

        Optional<User> userOptional = userStorage.findUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }*/

    @Test
    void contextLoads() {
    }

}