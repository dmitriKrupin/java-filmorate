package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Set;

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
class FilmDbStorageImplTest {
    private final FilmDbStorageImpl filmDbStorage;
    private final Set<Long> likesList = null;
    private final Set<Genre> genres = null;
    private final MPA mpa = new MPA(1, "G");
    private final Film film = new Film(1,
            "Film for test",
            "Description film for test",
            "1979-04-17",
            100, likesList, genres, mpa);
    @Test
    void addFilm() {
        filmDbStorage.addFilm(film);
        Assertions.assertEquals(film, filmDbStorage.findFilmById(1L));
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