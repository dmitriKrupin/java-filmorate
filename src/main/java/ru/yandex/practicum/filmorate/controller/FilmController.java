package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private final List<Film> filmsList = new ArrayList<>();

    private static long countFilmId = 1;

    @GetMapping("/films") //получение всех фильмов.
    public List<Film> filmsGetAll() {
        return filmsList;
    }

    @PostMapping(value = "/films") //добавление фильма;
    public Film createFilm(@Valid @RequestBody Film film) {
        film.setId(countFilmId++);
        if (validate(film)) {
            log.warn(String.format("Сохранение фильма с id = %d, наименование: %s", film.getId(), film.getName()));
            filmsList.add(film);
            return film;
        } else {
            throw new ValidationException("Ошибка при добавлении фильма " + film.getName() + " id: " + film.getId());
        }
    }

    @PutMapping(value = "/films") //обновление фильма;
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (validate(film)) {
            for (int i = 0; i < filmsList.size(); i++) {
                if (filmsList.get(i).getId() == film.getId()) {
                    log.warn(String.format("Обновление фильма с id = %d, наименование: %s", film.getId(), film.getName()));
                    filmsList.set(i, film);
                }
            }
        } else {
            throw new ValidationException("Ошибка при обновлении фильма " + film.getName() + " id: " + film.getId());
        }
        return film;
    }

    private boolean validate(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Ошибка! Название не может быть пустым!");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Ошибка! Максимальная длина описания больше 200 символов!");
        } else if (LocalDate.parse(film.getReleaseDate(), formatter).isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Ошибка! Дата релиза '" + film.getReleaseDate() + "' — должна быть не раньше 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("Ошибка! Продолжительность фильма должна быть положительной!");
        } else if (film.getId() <= 0) {
            throw new ValidationException("Ошибка! Такого id '" + film.getId() + "' не должно быть!");
        } else {
            return true;
        }
    }
}
