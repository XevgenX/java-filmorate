package ru.yandex.practicum.filmorate.storage.db_impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db_impl.mapper.GenreRowMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private static final String FIND_ALL_QUERY = """
        SELECT id, name
        FROM genres;""";
    private static final String FIND_BY_ID_QUERY = """
        SELECT id, name
        FROM genres
        WHERE id = ?;""";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(Long id) {
        Object[] objects = new Object[]{id};
        List<Genre> mpas = jdbcTemplate.query(FIND_BY_ID_QUERY, objects, new GenreRowMapper());
        if (Objects.isNull(mpas) || mpas.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mpas.get(0));
    }
}
