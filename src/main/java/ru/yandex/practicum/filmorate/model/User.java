package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

@Data
@NonNull
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private String birthday;
}
