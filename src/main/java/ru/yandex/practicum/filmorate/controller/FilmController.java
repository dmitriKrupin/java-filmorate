package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class FilmController {
    private final FilmService filmService;
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films") //получение всех фильмов.
    @ResponseBody
    public List<Film> filmsGetAll() {
        log.info("Получение списка всех фильмов");
        return filmService.getAllFilms();
    }

    // * 1. С помощью аннотации @PathVariable добавьте возможность получать каждый фильм и данные о пользователях
    // * по их уникальному идентификатору: GET .../films/{id}.
    @GetMapping(value = "/films/{id}")
    @ResponseBody
    public Film getFilm(@PathVariable long id) {
        if (id > 0) {
            log.info(String.format("Получаем данные фильма с id: %d", id));
            return filmService.findFilmById(id);
        } else {
            throw new NotFoundException("Ошибка при запросе фильма по идентификатору: " + id);
        }
    }

    // * 2.7. GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
    // * Если значение параметра count не задано, верните первые 10
    @GetMapping(value = "/films/popular")
    @ResponseBody
    public List<Film> getFilmsList(
            @RequestParam(value = "count", defaultValue = "10", required = false) long count) {
        if (count != 0) {
            log.info(String.format("Получаем список фильмов по количеству лайков: %d", count));
            return filmService.getTenPopularFilmsOfLikes(count);
        } else {
            //Если значение параметра count не задано, верните первые 10
            log.info("Получаем список первых 10 фильмов.");
            return filmService.getTenPopularFilmsOfLikes(10);
        }
    }

    @PostMapping(value = "/films") //добавление фильма;
    public Film createFilm(@Valid @RequestBody Film film) {
        if (validate(film)) {
            log.info(String.format("Сохранение фильма с id: %d, наименование: %s", film.getId(), film.getName()));
            return filmService.createFilm(film);
        } else {
            throw new ValidationException("Ошибка при добавлении фильма " + film.getName() + " id: " + film.getId());
        }
    }

    @PutMapping(value = "/films") //обновление фильма;
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (validate(film)) {
            log.info(String.format("Обновление фильма с id: %d, наименование: %s", film.getId(), film.getName()));
            filmService.updateFilm(film);
        } else {
            throw new ValidationException("Ошибка при обновлении фильма " + film.getName() + " id: " + film.getId());
        }
        return film;
    }

    //2.5. PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
    @PutMapping(value = "/films/{id}/like/{userId}")
    public Film addLikeForFilm(@PathVariable long id, @PathVariable long userId) {
        log.info(String.format("Добавление лайка фильму с id: %d, от пользователя с id: %s", id, userId));
        filmService.addLikeForFilm(id, userId);
        return filmService.findFilmById(id);
    }

    //2.6. DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeForFilm(@PathVariable long id, @PathVariable long userId) {
        log.info(String.format("Удаляем лайк пользователя с id: %d к фильму с id: %s", userId, id));
        filmService.deleteLikeForFilm(id, userId);
    }

    @GetMapping(value = "/genres") // GET /genres - получение списка всех жанров
    public List<Genre> getAllGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping(value = "/genres/{id}") //GET /genres/{id} - получение списка жанров по идентификатору
    public Genre getGenre(@PathVariable long id) {
        return filmService.getGenre(id);
    }

    @GetMapping(value = "/mpa")
    public List<MPA> getAllMPA() { // GET /mpa - получение рейтинга
        return filmService.getAllMPA();
    }

    @GetMapping(value = "/mpa/{id}")
    public MPA getMPA(@PathVariable long id) { // GET /mpa/{id} - получение рейтинга по идентификатору
        return filmService.getMPA(id);
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
        } else if (film.getId() < 0) {
            throw new NotFoundException("Ошибка! Такого id '" + film.getId() + "' не найдено!");
        } else {
            return true;
        }
    }
}