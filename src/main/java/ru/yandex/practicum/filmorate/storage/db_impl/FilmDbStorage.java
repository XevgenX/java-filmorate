package ru.yandex.practicum.filmorate.storage.db_impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db_impl.mapper.FilmWithConnectedDataExtractor;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Qualifier("film_storage_db_impl")
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private static final String FIND_ALL_QUERY = """
        SELECT f.id, f.name, f.description, f.release_date, f.duration,
        fl.user_id AS user_that_liked, f.mpaa_id, g.id as genre_id, g.name as genre_name
        FROM films f
        LEFT JOIN film_likes fl ON f.id = fl.film_id
        LEFT JOIN film_genres fg ON f.id = fg.film_id
        LEFT JOIN genres g ON g.id = fg.genre_id;""";
    private static final String FIND_BY_ID_QUERY = """
        SELECT f.id, f.name, f.description, f.release_date, f.duration,
        fl.user_id AS user_that_liked, f.mpaa_id, g.id as genre_id, g.name as genre_name
        FROM films f
        LEFT JOIN film_likes fl ON f.id = fl.film_id
        LEFT JOIN film_genres fg ON f.id = fg.film_id
        LEFT JOIN genres g ON g.id = fg.genre_id
        WHERE f.id = ?;""";
    private static final String CREATE_QUERY = """
        INSERT INTO films(name, description, release_date, duration, mpaa_id)
        VALUES (?, ?, ?, ?, ?);""";
    private static final String UPDATE_QUERY = """
            UPDATE films
            SET name = ?, description = ?, release_date = ?, duration = ?, mpaa_id = ?
            WHERE id = ?;""";
    private static final String DELETE_BY_ID_QUERY = """
            DELETE
            FROM films u
            WHERE u.id = ?""";
    private static final String ADD_LIKE_QUERY = """
            INSERT INTO film_likes(film_id, user_id)
            VALUES (?, ?);""";
    private static final String REMOVE_LIKE_QUERY = """
            DELETE
            FROM film_likes
            WHERE film_id = ? AND user_id = ?;""";
    private static final String SAVE_GENRE_QUERY = """
            INSERT INTO film_genres(film_id, genre_id)
            VALUES(?, ?)""";
    private static final String REMOVE_GENRES = """
            DELETE
            FROM film_genres
            WHERE film_id = ?;""";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, new FilmWithConnectedDataExtractor());
    }

    @Override
    public Optional<Film> findById(Long id) {
        Object[] objects = new Object[]{id};
        List<Film> films = jdbcTemplate.query(FIND_BY_ID_QUERY, objects, new FilmWithConnectedDataExtractor());
        if (Objects.isNull(films) || films.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(films.get(0));
    }

    @Override
    public Film save(Film film) {
        if (Objects.isNull(film.getId())) {
            return create(film);
        } else {
            return update(film);
        }
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
    }

    @Override
    public void addLike(Long filmId, User user) {
        Optional<Film> film = findById(filmId);
        if (film.isEmpty()) {
            throw new NotFoundException("Фильма с таким id не существует");
        }
        if (film.get().getLikes().contains(user.getId())) {
            removeLike(filmId, user);
        }
        jdbcTemplate.update(ADD_LIKE_QUERY, filmId, user.getId());
    }

    @Override
    public void removeLike(Long filmId, User user) {
        Optional<Film> film = findById(filmId);
        if (film.isEmpty()) {
            throw new NotFoundException("Фильма с таким id не существует");
        }
        jdbcTemplate.update(REMOVE_LIKE_QUERY, filmId, user.getId());
    }

    private Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(CREATE_QUERY, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setObject(3, film.getReleaseDate());
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        long id = keyHolder.getKey().longValue();
        film.setId(id);
        processGenres(film);
        return findById(id).orElseThrow();
    }

    private Film update(Film film) {
        jdbcTemplate.update(UPDATE_QUERY, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        processGenres(film);
        return findById(film.getId()).orElseThrow();
    }

    private void processGenres(Film film) {
        jdbcTemplate.update(REMOVE_GENRES, film.getId());
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(SAVE_GENRE_QUERY, film.getId(), genre.getId());
        }
    }
}
