package ru.practikum.kristinabogatova;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateUserTest {

    private final UserClient userClient = new UserClient();

    @Test
    @DisplayName("Создание нового уникального пользователя")
    @Description("Проверяю, что API успешно создает пользователя с уникальными данными")
    public void createUniqueUser() {
        String email = Utils.generateRandomEmail();
        String password = Utils.generateRandomPassword();
        String name = Utils.generateRandomName();

        Response response = userClient.createUser(email, password, name);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @DisplayName("Создание пользователя уже зарегистрированного")
    @Description("Проверяю, что повторная регистрация возвращает ошибку")
    public void createExistingUser() {
        String email = Utils.generateRandomEmail();
        String password = Utils.generateRandomPassword();
        String name = Utils.generateRandomName();

        Response first = userClient.createUser(email, password, name);
        assertEquals(200, first.getStatusCode());

        Response second = userClient.createUser(email, password, name);
        assertEquals(403, second.getStatusCode());
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля")
    @Description("Проверяю, что API возвращает ошибку при отсутствии email")
    public void createUserMissingField() {
        String password = Utils.generateRandomPassword();
        String name = Utils.generateRandomName();

        Response response = userClient.createUser("", password, name);
        assertEquals(403, response.getStatusCode());
    }
}