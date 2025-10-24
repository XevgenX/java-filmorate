package ru.yandex.practicum.filmorate.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {
    private UserService service;
    private User testUserForCreate;
    private User testUserForUpdate;

    @BeforeEach
    void setUp() {
        service = new UserService(new InMemoryUserStorage(), new UserValidator());
        testUserForCreate = new User();
        testUserForCreate.setName("Evgenii");
        testUserForCreate.setLogin("evgen");
        testUserForCreate.setEmail("evgen@gmail.com");
        testUserForCreate.setBirthday(LocalDate.of(1985, 10, 11));
        testUserForUpdate = new User();
        testUserForUpdate.setId(1L);
        testUserForUpdate.setName("Eugeniy");
        testUserForUpdate.setLogin("eugen");
        testUserForUpdate.setEmail("eugen@yandex.ru");
        testUserForUpdate.setBirthday(LocalDate.of(1982, 11, 26));
    }

    @Test
    @DisplayName("Менеджер должен сохранять нового пользователя с корректными полями")
    void shouldSaveNewUserWithCorrectFields() {
        service.create(testUserForCreate);
        List<User> users = service.list();
        assertEquals(1, users.size());
        assertEquals(testUserForCreate.getName(), users.get(0).getName());
        assertEquals(testUserForCreate.getLogin(), users.get(0).getLogin());
        assertEquals(testUserForCreate.getEmail(), users.get(0).getEmail());
        assertEquals(testUserForCreate.getBirthday(), users.get(0).getBirthday());
    }

    @Test
    @DisplayName("Менеджер должен при сохранении нового пользователя заменять пустое  имя логином")
    void shouldSaveNewUserReplacingNameWithLoginIfNameIsEmpty() {
        testUserForCreate.setName(null);
        service.create(testUserForCreate);
        List<User> users = service.list();
        assertEquals(1, users.size());
        assertEquals(testUserForCreate.getLogin(), users.get(0).getName());
    }

    @Test
    @DisplayName("Менеджер при создании должен генерировать id начиная с 0")
    void shouldGenerateIdFrom0() {
        service.create(testUserForCreate);
        List<User> users = service.list();
        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());
    }

    @Test
    @DisplayName("Менеджер при создании должен инкрементировать  id")
    void shouldIncrementId() {
        service.create(testUserForCreate);
        service.create(testUserForCreate);
        List<User> users = service.list();
        assertEquals(2, users.size());
        assertEquals(2, users.get(1).getId());
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если пользователь null")
    void shouldThrowValidExOnNullUserOnCreate() {
        assertThrows(ValidationException.class, () -> service.create(null));
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у пользователя почта null")
    void shouldThrowValidExOnUserWithNullEmailOnCreate() {
        testUserForCreate.setEmail(null);
        assertThrows(ValidationException.class, () -> service.create(testUserForCreate));
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у пользователя почта пуста")
    void shouldThrowValidExOnUserWithEmptyEmailOnCreate() {
        testUserForCreate.setEmail("");
        assertThrows(ValidationException.class, () -> service.create(testUserForCreate));
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у пользователя почта не содержит @")
    void shouldThrowValidExOnUserWithEmailWithoutSobakaOnCreate() {
        testUserForCreate.setEmail("aaafff.ru");
        assertThrows(ValidationException.class, () -> service.create(testUserForCreate));
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у пользователя login null")
    void shouldThrowValidExOnUserWithNullLoginOnCreate() {
        testUserForCreate.setLogin(null);
        assertThrows(ValidationException.class, () -> service.create(testUserForCreate));
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у пользователя login пуст")
    void shouldThrowValidExOnUserWithEmptyLoginOnCreate() {
        testUserForCreate.setLogin("");
        assertThrows(ValidationException.class, () -> service.create(testUserForCreate));
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у пользователя login содержит пробелы")
    void shouldThrowValidExOnUserWithLoginWithSpacesOnCreate() {
        testUserForCreate.setLogin("evgen evgen");
        assertThrows(ValidationException.class, () -> service.create(testUserForCreate));
    }

    @Test
    @DisplayName("Менеджер при создании должен выбрасывать исключение если у пользователя день рождения в будущем")
    void shouldThrowValidExOnUserWithBirthDayInFutureOnCreate() {
        testUserForCreate.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> service.create(testUserForCreate));
    }

    @Test
    @DisplayName("Менеджер должен сохранять нового пользователя с датой рождения сегодня")
    void shouldSaveNewUserWithBirthDayToday() {
        testUserForCreate.setBirthday(LocalDate.now());
        service.create(testUserForCreate);
        List<User> users = service.list();
        assertEquals(1, users.size());
        assertEquals(testUserForCreate.getBirthday(), users.get(0).getBirthday());
    }

    @Test
    @DisplayName("Менеджер должен сохранять нового пользователя с датой рождения сегодня")
    void shouldSaveNewUserWithoutBirthDay() {
        testUserForCreate.setBirthday(null);
        service.create(testUserForCreate);
        List<User> users = service.list();
        assertEquals(1, users.size());
        assertEquals(testUserForCreate.getBirthday(), users.get(0).getBirthday());
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленного пользователя с корректными полями")
    void shouldSaveUpdatedUserWithCorrectFields() {
        service.create(testUserForCreate);
        service.update(testUserForUpdate);
        List<User> users = service.list();
        assertEquals(1, users.size());
        assertEquals(testUserForUpdate.getName(), users.get(0).getName());
        assertEquals(testUserForUpdate.getLogin(), users.get(0).getLogin());
        assertEquals(testUserForUpdate.getEmail(), users.get(0).getEmail());
        assertEquals(testUserForUpdate.getBirthday(), users.get(0).getBirthday());
    }

    @Test
    @DisplayName("Менеджер должен при сохранении обновленного пользователя заменять пустое  имя логином")
    void shouldSaveUpdatedUserReplacingNameWithLoginIfNameIsEmpty() {
        service.create(testUserForCreate);
        testUserForUpdate.setName(null);
        service.update(testUserForUpdate);
        List<User> users = service.list();
        assertEquals(1, users.size());
        assertEquals(testUserForUpdate.getLogin(), users.get(0).getName());
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если пользователь null")
    void shouldThrowValidExOnNullUserOnUpdate() {
        service.create(testUserForCreate);
        assertThrows(ValidationException.class, () -> service.update(null));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если пользователь с id null")
    void shouldThrowValidExOnUserWithNullIdOnUpdate() {
        service.create(testUserForCreate);
        testUserForUpdate.setId(null);
        assertThrows(NotFoundException.class, () -> service.update(testUserForUpdate));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если пользователь с id null")
    void shouldThrowValidExOnUserWithNotExistingIdOnUpdate() {
        service.create(testUserForCreate);
        testUserForUpdate.setId(2L);
        assertThrows(NotFoundException.class, () -> service.update(testUserForUpdate));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у пользователя почта null")
    void shouldThrowValidExOnUserWithNullEmailOnUpdate() {
        service.create(testUserForCreate);
        testUserForUpdate.setEmail(null);
        assertThrows(ValidationException.class, () -> service.update(testUserForUpdate));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у пользователя почта пуста")
    void shouldThrowValidExOnUserWithEmptyEmailOnUpdate() {
        service.create(testUserForCreate);
        testUserForUpdate.setEmail("");
        assertThrows(ValidationException.class, () -> service.update(testUserForUpdate));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у пользователя почта не содержит @")
    void shouldThrowValidExOnUserWithEmailWithoutSobakaOnUpdate() {
        service.create(testUserForCreate);
        testUserForUpdate.setEmail("aaafff.ru");
        assertThrows(ValidationException.class, () -> service.update(testUserForUpdate));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у пользователя login null")
    void shouldThrowValidExOnUserWithNullLoginOnUpdate() {
        service.create(testUserForCreate);
        testUserForUpdate.setLogin(null);
        assertThrows(ValidationException.class, () -> service.update(testUserForUpdate));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у пользователя login пуст")
    void shouldThrowValidExOnUserWithEmptyLoginOnUpdate() {
        service.create(testUserForCreate);
        testUserForUpdate.setLogin("");
        assertThrows(ValidationException.class, () -> service.update(testUserForUpdate));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у пользователя login содержит пробелы")
    void shouldThrowValidExOnUserWithLoginWithSpacesOnUpdate() {
        service.create(testUserForCreate);
        testUserForUpdate.setLogin("evgen evgen");
        assertThrows(ValidationException.class, () -> service.update(testUserForUpdate));
    }

    @Test
    @DisplayName("Менеджер при обновлении должен выбрасывать исключение если у пользователя день рождения в будущем")
    void shouldThrowValidExOnUserWithBirthDayInFutureOnUpdate() {
        service.create(testUserForCreate);
        testUserForUpdate.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> service.update(testUserForUpdate));
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленного пользователя с датой рождения сегодня")
    void shouldSaveUpdatedUserWithBirthDayToday() {
        service.create(testUserForCreate);
        testUserForUpdate.setBirthday(LocalDate.now());
        service.update(testUserForUpdate);
        List<User> users = service.list();
        assertEquals(1, users.size());
        assertEquals(testUserForUpdate.getBirthday(), users.get(0).getBirthday());
    }

    @Test
    @DisplayName("Менеджер должен сохранять обновленного пользователя с датой рождения сегодня")
    void shouldSaveUpdatedUserWithoutBirthDay() {
        service.create(testUserForCreate);
        testUserForUpdate.setBirthday(null);
        service.update(testUserForUpdate);
        List<User> users = service.list();
        assertEquals(1, users.size());
        assertEquals(testUserForUpdate.getBirthday(), users.get(0).getBirthday());
    }
}
