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

class FilmDbStorageImplTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addFilm() {
    }

    @Test
    void deleteFilm() {
    }

    @Test
    void updateFilm() {
    }

    @Test
    void findFilmById() {
    }

    @Test
    void getFilmsList() {
    }

    @Test
    void addLikeForFilm() {
    }

    @Test
    void deleteLikeForFilm() {
    }

    @Test
    void getTenPopularFilmsOfLikes() {
    }

    @Test
    void getGenre() {
    }

    @Test
    void getAllGenres() {
    }

    @Test
    void getAllMPA() {
    }

    @Test
    void getMPA() {
    }
}