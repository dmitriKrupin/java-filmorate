package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

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
public class UserController {
    private final UserService userService;
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users") //получение списка всех пользователей.
    public List<User> usersGetAll() {
        log.warn("Получаем список данных всех пользователей");
        return userService.usersGetAll();
    }

    // * 1. С помощью аннотации @PathVariable добавьте возможность получать каждый фильм и данные о пользователях
    // * по их уникальному идентификатору: GET .../users/{id}.
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable long id) {
        if (id > 0) {
            log.warn(String.format("Получаем данные пользователя с id: %d ", id));
            return userService.findUserById(id);
        } else {
            throw new NotFoundException("Ошибка при запросе пользователя по идентификатору: " + id);
        }
    }

    //2.3. GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    @GetMapping("/users/{id}/friends")
    public List<User> getFriendsList(@PathVariable long id) {
        log.warn(String.format("Получаем список друзей пользователя с id: %d", id));
        return userService.getFriendsList(id);
    }

    //2.4. GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsList(@PathVariable long id, @PathVariable long otherId) {
        log.warn(String.format("Получаем общий список друзей пользователя с id: %d и с id: %s", id, otherId));
        return userService.getCommonFriendsList(id, otherId);
    }

    @PostMapping(value = "/users") //создание пользователя;
    public User createUser(@Valid @RequestBody User user) {
        user.setId(userService.userIdCounter());
        if (validate(user)) {
            log.warn(String.format("Сохранение пользователя с id: %d, наименование: %s", user.getId(), user.getName()));
            return userService.createUser(user);
        } else {
            throw new ValidationException("Ошибка при добавлении пользователя " + user.getName() + " id: " + user.getId());
        }
    }

    @PutMapping(value = "/users") //обновление пользователя;
    public User updateUser(@Valid @RequestBody User user) {
        if (validate(user)) {
            for (int i = 0; i < userService.usersGetAll().size(); i++) {
                if (userService.usersGetAll().get(i).getId() == user.getId()) {
                    log.warn(String.format("Обновление пользователя с id: %d, наименование: %s", user.getId(), user.getName()));
                    userService.updateUser(user);
                }
            }
        } else {
            throw new ValidationException("Ошибка при обновлении пользователя " + user.getName() + " id: " + user.getId());
        }
        return user;
    }

    //2.1. PUT /users/{id}/friends/{friendId} — добавление в друзья.
    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if (id > 0 && friendId > 0) {
            log.warn(String.format("Добавление пользователя с id: %d, в друзья пользователю с id: %s", friendId, id));
            userService.addFriendsInFriendsList(id, friendId);
        } else {
            throw new NotFoundException("Ошибка при запросе пользователя по id: " + id + " и id друга: " + friendId);
        }
    }

    //2.2. DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void deleteFromFriendsList(@PathVariable Long id, @PathVariable Long friendId) {
        log.warn(String.format("Удаляем пользователя с id: %d из друзей пользователя с id: %s", id, friendId));
        userService.deleteFriendFromFriendsList(id, friendId);
    }

    private boolean validate(User user) {
        //логин не может быть пустым и содержать пробелы;
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            userService.minusUserIdCounter();
            throw new ValidationException("Ошибка! Логин не может быть пустым и содержать пробелы!");
        }
        //дата рождения не может быть в будущем.
        else if (LocalDate.parse(user.getBirthday(), formatter).isAfter(LocalDate.now())) {
            userService.minusUserIdCounter();
            throw new ValidationException("Ошибка! Дата рождения: '" + user.getBirthday() + "' не может быть в будущем!");
        }
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        else if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            return true;
        }
        //электронная почта не может быть пустой и должна содержать символ @;
        else if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            userService.minusUserIdCounter();
            throw new ValidationException("Ошибка! Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getId() <= 0) {
            //throw new ValidationException("Ошибка! Такого id '" + user.getId() + "' не должно быть!");
            throw new NotFoundException("Ошибка! Такого id '" + user.getId() + "' не найдено!");
        } else {
            return true;
        }
    }
}
