package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDateTime birthday;
}
