package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 3. Убедитесь, что ваше приложение возвращает корректные HTTP-коды.
 * 3.1. 400 — если ошибка валидации: ValidationException;
 * BAD_REQUEST(400, HttpStatus.Series.CLIENT_ERROR, "Bad Request"),
 * <p>
 * 3.2. 404 — для всех ситуаций, если искомый объект не найден;
 * NOT_FOUND(404, HttpStatus.Series.CLIENT_ERROR, "Not Found"),
 * <p>
 * 3.3. 500 — если возникло исключение.
 * INTERNAL_SERVER_ERROR(500, HttpStatus.Series.SERVER_ERROR, "Internal Server Error")
 */

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
        log.warn("Получение списка всех фильмов");
        return filmService.filmsGetAll();
    }

    // * 1. С помощью аннотации @PathVariable добавьте возможность получать каждый фильм и данные о пользователях
    // * по их уникальному идентификатору: GET .../films/{id}.
    @GetMapping(value = "/films/{id}")
    @ResponseBody
    public Film getFilm(@PathVariable long id) {
        if (id > 0) {
            log.warn(String.format("Получаем данные фильма с id: %d", id));
            return filmService.findFilmById(id);
        } else {
            throw new NotFoundException("Ошибка при запросе фильма по идентификатору: " + id);
        }
    }

    //  http://127.0.0.1:8080/posts
    //  http://127.0.0.1:8080/posts?sort=desc&size=128
    //  http://127.0.0.1:8080/posts?sort=asc&size=5&page=2

    //    @GetMapping("/posts")
    //    public List<Post> findAll(
    //            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
    //            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
    //            @RequestParam(value = "sort", defaultValue = "desc", required = false) String sort) {
    //
    //        if(!(sort.equals("asc") || sort.equals("desc"))){
    //            throw new IllegalArgumentException();
    //        }
    //        if(page < 0 || size <= 0){
    //            throw new IllegalArgumentException();
    //        }
    //
    //        Integer from = page * size;
    //        return postService.findAll(size, from, sort);
    //    }

    //    public List<Post> findAll(Integer size, Integer from, String sort) {
    //        return posts.stream().sorted((p0, p1) -> {
    //            int comp = p0.getCreationDate().compareTo(p1.getCreationDate()); //прямой порядок сортировки
    //            if(sort.equals("desc")){
    //                comp = -1 * comp; //обратный порядок сортировки
    //            }
    //            return comp;
    //        }).skip(from).limit(size).collect(Collectors.toList());
    //    }

    // * 2.7. GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
    // * Если значение параметра count не задано, верните первые 10
    @GetMapping(value = "/films/popular")
    @ResponseBody
    public List<Film> getFilmsList(
            @RequestParam(value = "count", defaultValue = "10", required = false) long count) {
        if (count != 0) {
            log.warn(String.format("Получаем список фильмов по количеству лайков: %d", count));
            return filmService.getTenPopularFilmsOfLikes(count);
        } else {
            //Если значение параметра count не задано, верните первые 10
            log.warn("Получаем список первых 10 фильмов.");
            return filmService.getTenPopularFilmsOfLikes(10);
        }
    }

    @PostMapping(value = "/films") //добавление фильма;
    public Film createFilm(@Valid @RequestBody Film film) {
        film.setId(filmService.filmIdCounter());
        if (validate(film)) {
            log.warn(String.format("Сохранение фильма с id: %d, наименование: %s", film.getId(), film.getName()));
            return filmService.createFilm(film);
        } else {
            throw new ValidationException("Ошибка при добавлении фильма " + film.getName() + " id: " + film.getId());
        }
    }

    @PutMapping(value = "/films") //обновление фильма;
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (validate(film)) {
            for (int i = 0; i < filmService.filmsGetAll().size(); i++) {
                if (filmService.filmsGetAll().get(i).getId() == film.getId()) {
                    log.warn(String.format("Обновление фильма с id: %d, наименование: %s", film.getId(), film.getName()));
                    filmService.filmsGetAll().set(i, film);
                }
            }
        } else {
            throw new ValidationException("Ошибка при обновлении фильма " + film.getName() + " id: " + film.getId());
        }
        return film;
    }

    //2.5. PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
    @PutMapping(value = "/films/{id}/like/{userId}")
    public Film addLikeForFilm(@PathVariable long id, @PathVariable long userId) {
        log.warn(String.format("Добавление лайка фильму с id: %d, от пользователя с id: %s", id, userId));
        filmService.addLikeForFilm(id, userId);
        return filmService.findFilmById(id);
    }

    //2.6. DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeForFilm(@PathVariable long id, @PathVariable long userId) {
        log.warn(String.format("Удаляем лайк пользователя с id: %d к фильму с id: %s", userId, id));
        filmService.deleteLikeForFilm(id, userId);
    }

    private boolean validate(Film film) {
        if (film.getName().isEmpty()) {
            filmService.minusFilmIdCounter();
            throw new ValidationException("Ошибка! Название не может быть пустым!");
        } else if (film.getDescription().length() > 200) {
            filmService.minusFilmIdCounter();
            throw new ValidationException("Ошибка! Максимальная длина описания больше 200 символов!");
        } else if (LocalDate.parse(film.getReleaseDate(), formatter).isBefore(LocalDate.of(1895, 12, 28))) {
            filmService.minusFilmIdCounter();
            throw new ValidationException("Ошибка! Дата релиза '" + film.getReleaseDate() + "' — должна быть не раньше 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            filmService.minusFilmIdCounter();
            throw new ValidationException("Ошибка! Продолжительность фильма должна быть положительной!");
        } else if (film.getId() <= 0) {
            throw new NotFoundException("Ошибка! Такого id '" + film.getId() + "' не найдено!");
        } else {
            return true;
        }
    }
}
