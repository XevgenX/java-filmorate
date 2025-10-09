package ru.yandex.practicum.filmorate.manager;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmManager {

    List<Film> list();

    Film create(Film film);

    Film update(Film film);
}
