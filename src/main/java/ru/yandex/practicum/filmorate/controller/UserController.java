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
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private final List<User> userList = new ArrayList<>();

    private static long countUserId = 1;

    @GetMapping("/users") //получение списка всех пользователей.
    public List<User> usersGetAll() {
        return userList;
    }

    @PostMapping(value = "/users") //создание пользователя;
    public User createUser(@Valid @RequestBody User user) {
        user.setId(countUserId++);
        if (validate(user)) {
            log.warn(String.format("Сохранение пользователя с id = %d, наименование: %s", user.getId(), user.getName()));
            userList.add(user);
            return user;
        } else {
            throw new ValidationException("Ошибка при добавлении фильма " + user.getName() + " id: " + user.getId());
        }
    }

    @PutMapping(value = "/users") //обновление пользователя;
    public User updateUser(@Valid @RequestBody User user) {
        if (validate(user)) {
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).getId() == user.getId()) {
                    log.warn(String.format("Обновление пользователя с id = %d, наименование: %s", user.getId(), user.getName()));
                    userList.set(i, user);
                }
            }
        } else {
            throw new ValidationException("Ошибка при обновлении фильма " + user.getName() + " id: " + user.getId());
        }
        return user;
    }

    private boolean validate(User user) {
        //логин не может быть пустым и содержать пробелы;
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Ошибка! Логин не может быть пустым и содержать пробелы!");
        }
        //дата рождения не может быть в будущем.
        else if (LocalDate.parse(user.getBirthday(), formatter).isAfter(LocalDate.now())) {
            throw new ValidationException("Ошибка! Дата рождения: '" + user.getBirthday() + "' не может быть в будущем!");
        }
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        else if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            return true;
        }
        //электронная почта не может быть пустой и должна содержать символ @;
        else if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Ошибка! Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getId() <= 0) {
            throw new ValidationException("Ошибка! Такого id '" + user.getId() + "' не должно быть!");
        } else {
            return true;
        }
    }
}
