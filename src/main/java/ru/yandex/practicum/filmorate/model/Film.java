package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.guide.Genre;
import ru.yandex.practicum.filmorate.guide.MPA;

import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode
public class Film implements Comparable<Film> {
    private long id;
    private String name; //название не может быть пустым;
    private String description; //максимальная длина описания — 200 символов;
    private String releaseDate; //дата релиза — не раньше 28 декабря 1895 года
    private long duration; //продолжительность фильма должна быть положительной.
    private Set<Long> likesList;
    private Set<Genre> genres; //у фильма может быть сразу несколько жанров, а у поля — несколько значений
    private MPA mpa; //оценка, определяющая возрастное ограничение для фильма

    @Override
    public int compareTo(Film o) {
        if (this.getId() == o.getId()) {
            return 0;
        }
        if (this.getLikesList() == null && o.getLikesList() == null) {
            return 1;
        }
        if (this.getLikesList() != null && o.getLikesList() == null) {
            return -1;
        }
        if (this.getLikesList() == null && o.getLikesList() != null) {
            return 1;
        }
        if (this.getLikesList().equals(o.getLikesList())) {
            return 0;
        }
        return 0;
    }
}
