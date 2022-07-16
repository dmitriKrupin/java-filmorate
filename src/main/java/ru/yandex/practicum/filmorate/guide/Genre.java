package ru.yandex.practicum.filmorate.guide;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Genre {
    long genreId;
    String genre;
}
