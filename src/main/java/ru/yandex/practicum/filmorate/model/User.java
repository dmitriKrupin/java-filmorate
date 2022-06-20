package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

@Data
//@NonNull
public class User {
    private long id;
    private String email; //электронная почта не может быть пустой и должна содержать символ @;
    private String login; //логин не может быть пустым и содержать пробелы;
    private String name; //имя для отображения может быть пустым — в таком случае будет использован логин;
    private String birthday; //дата рождения не может быть в будущем.
}
