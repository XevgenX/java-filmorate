package ru.yandex.practicum.filmorate.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceTest {
    private FilmService service;
    private Film testFilmForCreate;
    private Film testFilmForUpdate;

    @BeforeEach
    void setUp() {
        service = new FilmService(new InMemoryFilmStorage(), new FilmValidator());
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
        service.create(testFilmForCreate);
        List<Film> films = service.list();
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
        service.create(testFilmForCreate);
        List<Film> films = service.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(1, films.get(0).getId());
    }

    @Test
    @DisplayName("Менеджер при создании должен инкрементировать  id")
    void shouldIncrementId() {
        service.create(testFilmForCreate);
        service.create(testFilmForCreate);
        List<Film> films = service.list();
        assertNotNull(films);
        assertEquals(2, films.size());
        assertEquals(2, films.get(1).getId());
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если фильм null")
    void shouldThrowValidExOnNullFilmOnCreate() {
        assertThrows(ValidationException.class, () -> service.create(null));
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у фильма название null")
    void shouldThrowValidExOnFilmWithNullNameOnCreate() {
        testFilmForCreate.setName(null);
        assertThrows(ValidationException.class, () -> service.create(testFilmForCreate));
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у фильма название пустое")
    void shouldThrowValidExOnFilmWithEmptyNameOnCreate() {
        testFilmForCreate.setName("");
        assertThrows(ValidationException.class, () -> service.create(testFilmForCreate));
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у фильма со слишком большим описанием")
    void shouldThrowValidExOnFilmWithTooLargeDescriptionOnCreate() {
        testFilmForCreate.setDescription("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        assertThrows(ValidationException.class, () -> service.create(testFilmForCreate));
    }

    @Test
    @DisplayName("Менеджер должен сохранять новый фильм с описанием ровно 200 символов")
    void shouldSaveNewFilmWithDescriptionEq200() {
        testFilmForCreate.setDescription("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        service.create(testFilmForCreate);
        List<Film> films = service.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForCreate.getDescription(), films.get(0).getDescription());
    }

    @Test
    @DisplayName("Менеджер должен сохранять новый фильм с описанием null")
    void shouldSaveNewFilmWithNullDescription() {
        testFilmForCreate.setDescription(null);
        service.create(testFilmForCreate);
        List<Film> films = service.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForCreate.getDescription(), films.get(0).getDescription());
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у фильма с датой релиза до 28 декабря 1895 года")
    void shouldThrowValidExOnFilmWithReleaseDateBefore18951228OnCreate() {
        testFilmForCreate.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> service.create(testFilmForCreate));
    }

    @Test
    @DisplayName("Менеджер должен сохранять новый фильм с датой релиза ровно 28 декабря 1895 года")
    void shouldSaveNewFilmWithReleaseDateEq18951228() {
        testFilmForCreate.setReleaseDate(LocalDate.of(1895, 12, 28));
        service.create(testFilmForCreate);
        List<Film> films = service.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForCreate.getReleaseDate(), films.get(0).getReleaseDate());
    }

    @Test
    @DisplayName("Менеджер должен сохранять новый фильм с датой релиза null")
    void shouldSaveNewFilmWithNullReleaseDate() {
        testFilmForCreate.setReleaseDate(null);
        service.create(testFilmForCreate);
        List<Film> films = service.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForCreate.getReleaseDate(), films.get(0).getReleaseDate());
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у фильма с с отрицательной продолжительностью")
    void shouldThrowValidExOnFilmWithNegativeDurationOnCreate() {
        testFilmForCreate.setDuration(-1);
        assertThrows(ValidationException.class, () -> service.create(testFilmForCreate));
    }

    @Test
    @DisplayName("Менеджер должен сохранять новый фильм с продолжительностью равной 0")
    void shouldSaveNewFilmWithDuration0() {
        testFilmForCreate.setDuration(0);
        service.create(testFilmForCreate);
        List<Film> films = service.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(0, films.get(0).getDuration());
    }

    @Test
    @DisplayName("Менеджер должен сохранять новый фильм с продолжительностью null")
    void shouldSaveNewFilmWithNullDuration() {
        testFilmForCreate.setDuration(null);
        service.create(testFilmForCreate);
        List<Film> films = service.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForCreate.getDuration(), films.get(0).getDuration());
    }

    @Test
    @DisplayName("Менеджер должен обновить существующий фильм с корректными полями")
    void shouldUpdateExistedFilmWithCorrectFields() {
        service.create(testFilmForCreate);
        service.update(testFilmForUpdate);
        List<Film> films = service.list();
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
        assertThrows(ValidationException.class, () -> service.update(null));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если id фильма null")
    void shouldThrowValidExOnFilmWithNullIdOnUpdate() {
        testFilmForUpdate.setId(null);
        assertThrows(ValidationException.class, () -> service.update(null));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если id фильма не существует")
    void shouldThrowValidExOnFilmWithNotExistingIdOnUpdate() {
        service.create(testFilmForCreate);
        testFilmForUpdate.setId(2L);
        assertThrows(ValidationException.class, () -> service.update(null));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у фильма название null")
    void shouldThrowValidExOnFilmWithNullNameOnUpdate() {
        service.create(testFilmForCreate);
        testFilmForUpdate.setName(null);
        assertThrows(ValidationException.class, () -> service.update(testFilmForUpdate));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у фильма название пустое")
    void shouldThrowValidExOnFilmWithEmptyNameOnUpdate() {
        service.create(testFilmForCreate);
        testFilmForUpdate.setName("");
        assertThrows(ValidationException.class, () -> service.update(testFilmForUpdate));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у фильма со слишком большим описанием")
    void shouldThrowValidExOnFilmWithTooLargeDescriptionOnUpdate() {
        service.create(testFilmForCreate);
        testFilmForUpdate.setDescription("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        assertThrows(ValidationException.class, () -> service.update(testFilmForUpdate));
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленный фильм с описанием ровно 200 символов")
    void shouldSaveUpdatedFilmWithDescriptionEq200() {
        service.create(testFilmForCreate);
        testFilmForUpdate.setDescription("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        service.update(testFilmForUpdate);
        List<Film> films = service.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForUpdate.getDescription(), films.get(0).getDescription());
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленный фильм с описанием null")
    void shouldSaveUpdatedFilmWithNullDescription() {
        service.create(testFilmForCreate);
        testFilmForUpdate.setDescription(null);
        service.update(testFilmForUpdate);
        List<Film> films = service.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForUpdate.getDescription(), films.get(0).getDescription());
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у фильма с датой релиза до 28 декабря 1895 года")
    void shouldThrowValidExOnFilmWithReleaseDateBefore18951228OnUpdate() {
        service.create(testFilmForCreate);
        testFilmForUpdate.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> service.update(testFilmForUpdate));
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленный фильм с датой релиза ровно 28 декабря 1895 года")
    void shouldSaveUpdatedFilmWithReleaseDateEq18951228() {
        service.create(testFilmForCreate);
        testFilmForUpdate.setReleaseDate(LocalDate.of(1895, 12, 28));
        service.update(testFilmForUpdate);
        List<Film> films = service.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(LocalDate.of(1895, 12, 28), films.get(0).getReleaseDate());
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленный фильм с датой релиза null")
    void shouldSaveUpdatedFilmWithNullReleaseDate() {
        service.create(testFilmForCreate);
        testFilmForUpdate.setReleaseDate(null);
        service.update(testFilmForUpdate);
        List<Film> films = service.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForUpdate.getReleaseDate(), films.get(0).getReleaseDate());
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у фильма с с отрицательной продолжительностью")
    void shouldThrowValidExOnFilmWithNegativeDurationOnUpdate() {
        service.create(testFilmForCreate);
        testFilmForUpdate.setDuration(-1);
        assertThrows(ValidationException.class, () -> service.update(testFilmForUpdate));
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленный фильм с продолжительностью равной 0")
    void shouldSaveUpdatedFilmWithDuration0() {
        service.create(testFilmForCreate);
        testFilmForUpdate.setDuration(0);
        service.update(testFilmForUpdate);
        List<Film> films = service.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(0, films.get(0).getDuration());
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленный фильм с продолжительностью null")
    void shouldSaveUpdatedFilmWithNullDuration() {
        service.create(testFilmForCreate);
        testFilmForUpdate.setDuration(null);
        service.update(testFilmForUpdate);
        List<Film> films = service.list();
        assertNotNull(films);
        assertEquals(1, films.size());
        assertEquals(testFilmForUpdate.getDuration(), films.get(0).getDuration());
    }

    @Test
    @DisplayName("Должен корректно сортировать фильмы по лайкам")
    void shouldSortCorrectly() {
        var film1 = new Film();
        film1.setName("zk69anb5UPpPViW");
        film1.setDescription("Vd7ffIABJMn5Bnp5daTzT4oOXeqLjJ9hXHJ1Bz9d6FXxoVnkYY");
        film1.setDuration(132);
        film1.setReleaseDate(LocalDate.of(1985, 1, 25));
        service.create(film1);
        var film2 = new Film();
        film2.setName("hkRlLwz6haFA7m4");
        film2.setDescription("MmI95SMQq5063X03wifvacq9O7XyqBybNX72HwHxBrXOFABq98");
        film2.setDuration(64);
        film2.setReleaseDate(LocalDate.of(1994, 5, 11));
        service.create(film2);
        var film3 = new Film();
        film3.setName("GWxDKP1qFa6C3PP");
        film3.setDescription("dpEk21LTfUgLcWIA2OQWrK8fltzarEZA1naTVZygGMdHyIfFKq");
        film3.setDuration(79);
        film3.setReleaseDate(LocalDate.of(1972, 2, 21));
        service.create(film3);
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        User user3 = new User();
        user3.setId(3L);
        service.addLike(1L, user1);
        service.addLike(2L, user1);
        service.addLike(2L, user2);
        service.addLike(3L, user1);
        service.addLike(3L, user3);
        service.addLike(3L, user2);
        List<Film> popularFilms = service.getMostPopularFilms(1000);
        assertEquals(3, popularFilms.size());
        assertEquals(film3, popularFilms.get(0));
        assertEquals(film2, popularFilms.get(1));
        assertEquals(film1, popularFilms.get(2));
    }
}
