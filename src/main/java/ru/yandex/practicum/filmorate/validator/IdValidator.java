package ru.yandex.practicum.filmorate.validator;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Optional;

@Component
public class IdValidator {
    public void validate(Long id) {
        if (id == null) {
            throw new ValidationException("id не может быть null");
        }
    }

    public void validate(Optional<Long> id) {
        if (id.isEmpty()) {
            throw new ValidationException("id не может быть null");
        }
    }
}