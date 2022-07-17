package ru.yandex.practicum.filmorate.guide;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Genre implements Comparable<Genre> {
    long id;
    String name;

    @Override
    public int compareTo(Genre o) {
        return (int) (this.getId() - o.getId());
    }
}