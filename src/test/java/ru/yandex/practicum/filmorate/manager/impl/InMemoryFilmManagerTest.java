package ru.yandex.practicum.filmorate.manager.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.manager.FilmManager;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryFilmManagerTest {
    private FilmManager manager;
    private Film testFilmForCreate;
    private Film testFilmForUpdate;

    @BeforeEach
    void setUp() {
        manager = new InMemoryFilmManager(new FilmValidator());
        testFilmForCreate = new Film();
        testFilmForCreate.setName("Вначале");
        testFilmForCreate.setDescription("Приквел Вавилона 5");
        testFilmForCreate.setDuration(94);
        testFilmForCreate.setReleaseDate(LocalDate.of(1998, 1, 4));
        testFilmForUpdate = new Film();
        testFilmForUpdate.setId(1L);
        testFilmForUpdate.setName("Третье пространство");
        testFilmForUpdate.setDescription("Продолжение 4 сезона Вавилона 5");
        testFilmForUpdate.setDuration(94);
        testFilmForUpdate.setReleaseDate(LocalDate.of(1998, 7, 19));
    }

    @Test
    @DisplayName("Менеджер должен сохранять новый фильм с корректными полями")
    void shouldSaveNewFilmWithCorrectFields() {
        manager.create(testFilmForCreate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForCreate.getName(), films.get(0).getName());
        assertEquals(testFilmForCreate.getDescription(), films.get(0).getDescription());
        assertEquals(testFilmForCreate.getDuration(), films.get(0).getDuration());
        assertEquals(testFilmForCreate.getReleaseDate(), films.get(0).getReleaseDate());
    }

    @Test
    @DisplayName("Менеджер при создании должен генерировать id начиная с 0")
    void shouldGenerateIdFrom0() {
        manager.create(testFilmForCreate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(1, films.get(0).getId());
    }

    @Test
    @DisplayName("Менеджер при создании должен инкрементировать  id")
    void shouldIncrementId() {
        manager.create(testFilmForCreate);
        manager.create(testFilmForCreate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(2, films.size());
        assertEquals(2, films.get(1).getId());
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если фильм null")
    void shouldThrowValidExOnNullFilmOnCreate() {
        assertThrows(ValidationException.class, () -> manager.create(null));
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у фильма название null")
    void shouldThrowValidExOnFilmWithNullNameOnCreate() {
        testFilmForCreate.setName(null);
        assertThrows(ValidationException.class, () -> manager.create(testFilmForCreate));
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у фильма название пустое")
    void shouldThrowValidExOnFilmWithEmptyNameOnCreate() {
        testFilmForCreate.setName("");
        assertThrows(ValidationException.class, () -> manager.create(testFilmForCreate));
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у фильма со слишком большим описанием")
    void shouldThrowValidExOnFilmWithTooLargeDescriptionOnCreate() {
        testFilmForCreate.setDescription("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        assertThrows(ValidationException.class, () -> manager.create(testFilmForCreate));
    }

    @Test
    @DisplayName("Менеджер должен сохранять новый фильм с описанием ровно 200 символов")
    void shouldSaveNewFilmWithDescriptionEq200() {
        testFilmForCreate.setDescription("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        manager.create(testFilmForCreate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForCreate.getDescription(), films.get(0).getDescription());
    }

    @Test
    @DisplayName("Менеджер должен сохранять новый фильм с описанием null")
    void shouldSaveNewFilmWithNullDescription() {
        testFilmForCreate.setDescription(null);
        manager.create(testFilmForCreate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForCreate.getDescription(), films.get(0).getDescription());
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у фильма с датой релиза до 28 декабря 1895 года")
    void shouldThrowValidExOnFilmWithReleaseDateBefore18951228OnCreate() {
        testFilmForCreate.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> manager.create(testFilmForCreate));
    }

    @Test
    @DisplayName("Менеджер должен сохранять новый фильм с датой релиза ровно 28 декабря 1895 года")
    void shouldSaveNewFilmWithReleaseDateEq18951228() {
        testFilmForCreate.setReleaseDate(LocalDate.of(1895, 12, 28));
        manager.create(testFilmForCreate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForCreate.getReleaseDate(), films.get(0).getReleaseDate());
    }

    @Test
    @DisplayName("Менеджер должен сохранять новый фильм с датой релиза null")
    void shouldSaveNewFilmWithNullReleaseDate() {
        testFilmForCreate.setReleaseDate(null);
        manager.create(testFilmForCreate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForCreate.getReleaseDate(), films.get(0).getReleaseDate());
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у фильма с с отрицательной продолжительностью")
    void shouldThrowValidExOnFilmWithNegativeDurationOnCreate() {
        testFilmForCreate.setDuration(-1);
        assertThrows(ValidationException.class, () -> manager.create(testFilmForCreate));
    }

    @Test
    @DisplayName("Менеджер должен сохранять новый фильм с продолжительностью равной 0")
    void shouldSaveNewFilmWithDuration0() {
        testFilmForCreate.setDuration(0);
        manager.create(testFilmForCreate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(0, films.get(0).getDuration());
    }

    @Test
    @DisplayName("Менеджер должен сохранять новый фильм с продолжительностью null")
    void shouldSaveNewFilmWithNullDuration() {
        testFilmForCreate.setDuration(null);
        manager.create(testFilmForCreate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForCreate.getDuration(), films.get(0).getDuration());
    }

    @Test
    @DisplayName("Менеджер должен обновить существующий фильм с корректными полями")
    void shouldUpdateExistedFilmWithCorrectFields() {
        manager.create(testFilmForCreate);
        manager.update(testFilmForUpdate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForUpdate.getName(), films.get(0).getName());
        assertEquals(testFilmForUpdate.getDescription(), films.get(0).getDescription());
        assertEquals(testFilmForUpdate.getDuration(), films.get(0).getDuration());
        assertEquals(testFilmForUpdate.getReleaseDate(), films.get(0).getReleaseDate());
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если фильм null")
    void shouldThrowValidExOnNullFilmOnUpdate() {
        assertThrows(ValidationException.class, () -> manager.update(null));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если id фильма null")
    void shouldThrowValidExOnFilmWithNullIdOnUpdate() {
        testFilmForUpdate.setId(null);
        assertThrows(ValidationException.class, () -> manager.update(null));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если id фильма не существует")
    void shouldThrowValidExOnFilmWithNotExistingIdOnUpdate() {
        manager.create(testFilmForCreate);
        testFilmForUpdate.setId(2L);
        assertThrows(ValidationException.class, () -> manager.update(null));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у фильма название null")
    void shouldThrowValidExOnFilmWithNullNameOnUpdate() {
        manager.create(testFilmForCreate);
        testFilmForUpdate.setName(null);
        assertThrows(ValidationException.class, () -> manager.update(testFilmForUpdate));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у фильма название пустое")
    void shouldThrowValidExOnFilmWithEmptyNameOnUpdate() {
        manager.create(testFilmForCreate);
        testFilmForUpdate.setName("");
        assertThrows(ValidationException.class, () -> manager.update(testFilmForUpdate));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у фильма со слишком большим описанием")
    void shouldThrowValidExOnFilmWithTooLargeDescriptionOnUpdate() {
        manager.create(testFilmForCreate);
        testFilmForUpdate.setDescription("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        assertThrows(ValidationException.class, () -> manager.update(testFilmForUpdate));
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленный фильм с описанием ровно 200 символов")
    void shouldSaveUpdatedFilmWithDescriptionEq200() {
        manager.create(testFilmForCreate);
        testFilmForUpdate.setDescription("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        manager.update(testFilmForUpdate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForUpdate.getDescription(), films.get(0).getDescription());
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленный фильм с описанием null")
    void shouldSaveUpdatedFilmWithNullDescription() {
        manager.create(testFilmForCreate);
        testFilmForUpdate.setDescription(null);
        manager.update(testFilmForUpdate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForUpdate.getDescription(), films.get(0).getDescription());
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у фильма с датой релиза до 28 декабря 1895 года")
    void shouldThrowValidExOnFilmWithReleaseDateBefore18951228OnUpdate() {
        manager.create(testFilmForCreate);
        testFilmForUpdate.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> manager.update(testFilmForUpdate));
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленный фильм с датой релиза ровно 28 декабря 1895 года")
    void shouldSaveUpdatedFilmWithReleaseDateEq18951228() {
        manager.create(testFilmForCreate);
        testFilmForUpdate.setReleaseDate(LocalDate.of(1895, 12, 28));
        manager.update(testFilmForUpdate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(LocalDate.of(1895, 12, 28), films.get(0).getReleaseDate());
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленный фильм с датой релиза null")
    void shouldSaveUpdatedFilmWithNullReleaseDate() {
        manager.create(testFilmForCreate);
        testFilmForUpdate.setReleaseDate(null);
        manager.update(testFilmForUpdate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForUpdate.getReleaseDate(), films.get(0).getReleaseDate());
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у фильма с с отрицательной продолжительностью")
    void shouldThrowValidExOnFilmWithNegativeDurationOnUpdate() {
        manager.create(testFilmForCreate);
        testFilmForUpdate.setDuration(-1);
        assertThrows(ValidationException.class, () -> manager.update(testFilmForUpdate));
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленный фильм с продолжительностью равной 0")
    void shouldSaveUpdatedFilmWithDuration0() {
        manager.create(testFilmForCreate);
        testFilmForUpdate.setDuration(0);
        manager.update(testFilmForUpdate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(0, films.get(0).getDuration());
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленный фильм с продолжительностью null")
    void shouldSaveUpdatedFilmWithNullDuration() {
        manager.create(testFilmForCreate);
        testFilmForUpdate.setDuration(null);
        manager.update(testFilmForUpdate);
        List<Film> films = manager.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForUpdate.getDuration(), films.get(0).getDuration());
    }
}
