package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
//@NotNull
public class Film {
    private long id;
    private String name; //название не может быть пустым;
    private String description; //максимальная длина описания — 200 символов;
    private String releaseDate; //дата релиза — не раньше 28 декабря 1895 года
    private long duration; //продолжительность фильма должна быть положительной.
}
