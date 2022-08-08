package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Genre implements Comparable<Genre> {
    private long id;
    private String name;

    @Override
    public int compareTo(Genre o) {
        return (int) (this.getId() - o.getId());
    }
}