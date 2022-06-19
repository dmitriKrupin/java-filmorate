package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FilmController {
    //todo: Эндпоинты для создания и обновления данных должны также вернуть созданную или изменённую сущность.

    private final List<Film> filmsList = new ArrayList<>();

    @GetMapping("/films") //получение всех фильмов.
    public List<Film> filmGetAll() {
        return filmsList;
    }

    @PostMapping(value = "/films") //добавление фильма;
    public void createFilm(@RequestBody Film film) {
        filmsList.add(film);
    }

    @PutMapping(value = "/films") //обновление фильма;
    public void updateFilm(@RequestBody Film film) {
        filmsList.add(film);
    }
}
