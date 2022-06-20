package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private final List<User> userList = new ArrayList<>();

    static long countUserId = 1;

    @GetMapping("/users") //получение списка всех пользователей.
    public List<User> usersGetAll() {
        return userList;
    }

    @PostMapping(value = "/users") //создание пользователя;
    public void createUser(@Valid @RequestBody User user) {
        user.setId(countUserId++);
        if (validate(user)) {
            log.warn("Логирование при создании пользователя!");
            userList.add(user);
        } else {
            throw new ValidationException("Ошибка при добавлении пользователя!");
        }
    }

    @PutMapping(value = "/users") //обновление пользователя;
    public void updateUser(@Valid @RequestBody User user) {
        if (validate(user)) {
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).getId() == user.getId()) {
                    log.warn("Логирование при обновлении пользователя!");
                    userList.set(i, user);
                }
            }
        } else {
            throw new ValidationException("Ошибка при обновлении пользователя!");
        }
    }

    private boolean validate(User user) {
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Ошибка! Логин не может быть пустым и содержать пробелы!");
        } else if (LocalDate.parse(user.getBirthday(), formatter).isAfter(LocalDate.now())) {
            throw new ValidationException("Ошибка! Дата рождения не может быть в будущем!");
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            throw new ValidationException("Ошибка! Имя не может быть пустым!");
        } else if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Ошибка! Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getId() <= 0) {
            throw new ValidationException("Ошибка! Такого id не должно быть!");
        } else {
            return true;
        }
    }
}
