package ru.practikum.kristinabogatova;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoginUserTest {

    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        accessToken = null;
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Логин существующего пользователя")
    @Description("Проверяю, что существующий пользователь может войти успешно")
    public void loginExistingUser() {
        String email = Utils.generateRandomEmail();
        String password = Utils.generateRandomPassword();
        String name = Utils.generateRandomName();

        Response create = userClient.createUser(email, password, name);
        assertEquals("Пользователь не был создан", 200, create.getStatusCode());
        accessToken = create.jsonPath().getString("accessToken");

        Response login = userClient.loginUser(email, password);
        assertEquals("Логин существующего пользователя не прошёл", 200, login.getStatusCode());
    }

    @Test
    @DisplayName("Логин с неверным email")
    @Description("Проверяю, что API возвращает 401 при неверном email")
    public void loginWithWrongEmail() {
        String fakeEmail = "fake_" + Utils.generateRandomEmail();
        Response response = userClient.loginUser(fakeEmail, "anyPassword123");
        assertEquals("Неверный email не возвращает 401", 401, response.getStatusCode());
        // Пользователь не создавался, токен не сохраняем
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Проверяю, что API возвращает 401 при неверном пароле")
    public void loginWithWrongPassword() {
        String email = Utils.generateRandomEmail();
        String password = Utils.generateRandomPassword();
        String name = Utils.generateRandomName();

        Response create = userClient.createUser(email, password, name);
        assertEquals("Пользователь не был создан", 200, create.getStatusCode());
        accessToken = create.jsonPath().getString("accessToken");

        Response response = userClient.loginUser(email, "wrongpassword123");
        assertEquals("Неверный пароль не возвращает 401", 401, response.getStatusCode());
    }
}