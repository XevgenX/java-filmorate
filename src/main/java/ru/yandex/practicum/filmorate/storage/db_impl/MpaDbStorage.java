package ru.yandex.practicum.filmorate.storage.db_impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.db_impl.mapper.MpaRowMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private static final String FIND_ALL_QUERY = """
        SELECT id, name
        FROM mpaas;""";
    private static final String FIND_BY_ID_QUERY = """
        SELECT id, name
        FROM mpaas
        WHERE id = ?;""";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, new MpaRowMapper());
    }

    @Override
    public Optional<Mpa> findById(Long id) {
        Object[] objects = new Object[]{id};
        List<Mpa> mpas = jdbcTemplate.query(FIND_BY_ID_QUERY, objects, new MpaRowMapper());
        if (Objects.isNull(mpas) || mpas.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mpas.get(0));
    }
}
