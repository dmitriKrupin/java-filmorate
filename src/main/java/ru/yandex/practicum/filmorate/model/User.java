package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import java.sql.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long id;
    @Email
    private String email; //электронная почта не может быть пустой и должна содержать символ @;
    private String login; //логин не может быть пустым и содержать пробелы;
    private String name; //имя для отображения может быть пустым — в таком случае будет использован логин;
    private String birthday; //дата рождения не может быть в будущем.
    private Set<Long> friendsIdList; //список id друзей
    private boolean statusApplicationFriend; //статус для связи «дружба» между двумя пользователями
}
