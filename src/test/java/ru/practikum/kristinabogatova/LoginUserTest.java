package ru.practikum.kristinabogatova;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.practikum.kristinabogatova.client.UserClient;

import static org.junit.Assert.assertEquals;

public class LoginUserTest {

    private UserClient userClient;
    private String email;
    private String password;
    private String name;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        email = RandomDataUtils.generateRandomEmail();
        password = RandomDataUtils.generateRandomPassword();
        name = RandomDataUtils.generateRandomName();
        Response create = userClient.createUser(email, password, name);
        accessToken = create.jsonPath().getString("accessToken");
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
        Response login = userClient.loginUser(email, password);

        assertEquals("Логин существующего пользователя прошел успешно", 200, login.getStatusCode());
    }

    @Test
    @DisplayName("Логин с неверным email")
    @Description("Проверяю, что API возвращает 401 при неверном email")
    public void loginWithWrongEmail() {
        String fakeEmail = "fake_" + RandomDataUtils.generateRandomEmail();

        Response response = userClient.loginUser(fakeEmail, password);

        assertEquals("Неверный email не возвращает 401", 401, response.getStatusCode());
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Проверяю, что API возвращает 401 при неверном пароле")
    public void loginWithWrongPassword() {
        Response response = userClient.loginUser(email, "wrongpassword123");

        assertEquals("Неверный пароль не возвращает 401", 401, response.getStatusCode());
    }
}