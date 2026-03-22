package ru.practikum.kristinabogatova;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import ru.practikum.kristinabogatova.client.UserClient;

import static org.junit.Assert.assertEquals;

public class CreateUserTest {

    private final UserClient userClient = new UserClient();

    @Test
    @DisplayName("Создание нового уникального пользователя")
    @Description("Проверяю, что API успешно создает пользователя с уникальными данными")
    public void createUniqueUser() {
        String email = RandomDataUtils.generateRandomEmail();
        String password = RandomDataUtils.generateRandomPassword();
        String name = RandomDataUtils.generateRandomName();

        Response response = userClient.createUser(email, password, name);

        assertEquals(200, response.getStatusCode());
    }

    @Test
    @DisplayName("Создание пользователя уже зарегистрированного")
    @Description("Проверяю, что повторная регистрация возвращает ошибку")
    public void createExistingUser() {
        String email = RandomDataUtils.generateRandomEmail();
        String password = RandomDataUtils.generateRandomPassword();
        String name = RandomDataUtils.generateRandomName();

        Response first = userClient.createUser(email, password, name);
        Response second = userClient.createUser(email, password, name);

        assertEquals(200, first.getStatusCode());
        assertEquals(403, second.getStatusCode());
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверяю, что API возвращает ошибку в теле ответа при отсутствии email")
    public void createUserMissingEmail() {
        String password = RandomDataUtils.generateRandomPassword();
        String name = RandomDataUtils.generateRandomName();

        Response response = userClient.createUser(null, password, name);

        assertEquals(403, response.getStatusCode());
    }

    @Test
    @DisplayName("Создание пользователя без password")
    @Description("Проверяю, что API возвращает ошибку в теле ответа при отсутствии password")
    public void createUserMissingPassword() {
        String email = RandomDataUtils.generateRandomEmail();
        String name = RandomDataUtils.generateRandomName();

        Response response = userClient.createUser(email, null, name);

        assertEquals(403, response.getStatusCode());
    }

    @Test
    @DisplayName("Создание пользователя без name")
    @Description("Проверяю, что API возвращает ошибку в теле ответа при отсутствии name")
    public void createUserMissingName() {
        String email = RandomDataUtils.generateRandomEmail();
        String password = RandomDataUtils.generateRandomPassword();

        Response response = userClient.createUser(email, password, null);

        assertEquals(403, response.getStatusCode());
    }
}