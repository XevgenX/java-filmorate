package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
@Component
public class FilmValidator {
    public void validate(Film film) {
        if (film == null) {
            log.error("Фильм не может быть null");
            throw new ValidationException("Фильм не может быть null");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() != null) {
            if (film.getDescription().length() > 200) {
                log.error("Максимальная длина описания — 200 символов");
                throw new ValidationException("Максимальная длина описания — 200 символов");
            }
        }
        if (film.getReleaseDate() != null) {
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.error("Дата релиза — не раньше 28 декабря 1895 года");
                throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
            }
        }
        if (film.getDuration() != null) {
            if (film.getDuration() < 0) {
                log.error("Продолжительность фильма должна быть положительным числом");
                throw new ValidationException("Продолжительность фильма должна быть положительным числом");
            }
        }
    }
}