package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    //todo: Эндпоинты для создания и обновления данных должны также вернуть созданную или изменённую сущность.

    private final List<User> userList = new ArrayList<>();

    @GetMapping("/users") //получение списка всех пользователей.
    public List<User> userGetAll() {
        return userList;
    }

    @PostMapping(value = "/users") //создание пользователя;
    public void createUser(@RequestBody User user) {
        userList.add(user);
    }

    @PutMapping(value = "/users") //обновление пользователя;
    public void updateUser(@RequestBody User user) {
        userList.add(user);
    }
}
