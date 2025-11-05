package ru.yandex.practicum.filmorate.storage.db_impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.storage.db_impl"})
public class MpaDbStorageTest {
    private final MpaDbStorage storage;

    @Test
    public void testFindMpaById() {
        Optional<Mpa> mpa = storage.findById(1L);
        assertThat(mpa)
                .isPresent()
                .hasValueSatisfying(u -> assertThat(u).hasFieldOrPropertyWithValue("name", "G"));
    }
}
