package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;

/**
 * 3. Убедитесь, что ваше приложение возвращает корректные HTTP-коды.
 * 3.1. 400 — если ошибка валидации: ValidationException;
 * BAD_REQUEST(400, HttpStatus.Series.CLIENT_ERROR, "Bad Request"),
 * <p>
 * 3.2. 404 — для всех ситуаций, если искомый объект не найден;
 * NOT_FOUND(404, HttpStatus.Series.CLIENT_ERROR, "Not Found"),
 * <p>
 * 3.3. 500 — если возникло исключение.
 * INTERNAL_SERVER_ERROR(500, HttpStatus.Series.SERVER_ERROR, "Internal Server Error")
 */

@RestControllerAdvice
@Data
public class ErrorController {
    // название ошибки
    //String error;
    // подробное описание
    //String description;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NotFoundException notFoundException(final NotFoundException e) {
        return new NotFoundException(
                String.format("Ошибка с полем \"%s\".", e.getParameter())
        );
    }
}
