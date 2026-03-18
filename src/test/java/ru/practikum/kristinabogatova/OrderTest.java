package ru.practikum.kristinabogatova;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OrderTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private String email;
    private String password;
    private String name;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();

        email = Utils.generateRandomEmail();
        password = Utils.generateRandomPassword();
        name = Utils.generateRandomName();

        Response createResp = userClient.createUser(email, password, name);
        assertEquals(200, createResp.getStatusCode());
        accessToken = createResp.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Проверяю, что авторизованный пользователь может создать заказ")
    public void createOrderWithAuthAndIngredients() {
        String[] ingredients = Utils.getValidIngredientHashes();
        Response response = orderClient.createOrderWithAuth(accessToken, ingredients);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.jsonPath().get("order.number"));
    }

    @Test
    @DisplayName("Создание заказа без авторизации, но с ингредиентами")
    @Description("Проверяю, что неавторизованный пользователь может создать заказ")
    public void createOrderWithoutAuthAndIngredients() {
        String[] ingredients = Utils.getValidIngredientHashes();
        Response response = orderClient.createOrderWithoutAuth(ingredients);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов (с авторизацией)")
    @Description("Проверяю, что API возвращает ошибку при пустом списке ингредиентов")
    public void createOrderWithAuthNoIngredients() {
        String[] ingredients = {};
        Response response = orderClient.createOrderWithAuth(accessToken, ingredients);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверяю, что API возвращает ошибку при неверном хеше (ожидается 400 или 500)")
    public void createOrderWithInvalidHash() {
        String[] ingredients = {Utils.invalidIngredientsHash()};
        Response response = orderClient.createOrderWithAuth(accessToken, ingredients);
        int code = response.getStatusCode();
        assertTrue("Статус код должен быть ошибкой (>=400), но был " + code, code >= 400);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    @Description("Проверяю, что API возвращает ошибку при пустом списке ингредиентов и без авторизации")
    public void createOrderWithoutAuthNoIngredients() {
        String[] ingredients = {};
        Response response = orderClient.createOrderWithoutAuth(ingredients);
        assertEquals(400, response.getStatusCode());
    }
}