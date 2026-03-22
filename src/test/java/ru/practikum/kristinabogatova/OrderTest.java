package ru.practikum.kristinabogatova;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.practikum.kristinabogatova.client.IngredientClient;
import ru.practikum.kristinabogatova.client.OrderClient;
import ru.practikum.kristinabogatova.client.UserClient;

import java.util.List;

import static org.junit.Assert.*;

public class OrderTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private IngredientClient ingredientClient;
    private String email;
    private String password;
    private String name;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        ingredientClient = new IngredientClient();

        email = RandomDataUtils.generateRandomEmail();
        password = RandomDataUtils.generateRandomPassword();
        name = RandomDataUtils.generateRandomName();

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
        List<String> ingredients = ingredientClient.getTwoIngredientHashes();

        Response response = orderClient.createOrderWithAuth(accessToken, ingredients);

        assertEquals(200, response.getStatusCode());
        assertNotNull(response.jsonPath().get("order.number"));
    }

    @Test
    @DisplayName("Создание заказа без авторизации, но с ингредиентами")
    @Description("Проверяю, что неавторизованный пользователь может создать заказ")
    public void createOrderWithoutAuthAndIngredients() {
        List<String> ingredients = ingredientClient.getTwoIngredientHashes();

        Response response = orderClient.createOrderWithoutAuth(ingredients);

        assertEquals(200, response.getStatusCode());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов (с авторизацией)")
    @Description("Проверяю, что API возвращает ошибку при пустом списке ингредиентов")
    public void createOrderWithAuthNoIngredients() {
        List<String> ingredients = List.of();

        Response response = orderClient.createOrderWithAuth(accessToken, ingredients);

        assertEquals(400, response.getStatusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals("Ingredient ids must be provided", response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверяю, что API возвращает ошибку при неверном хеше (ожидается 400 или 500)")
    public void createOrderWithInvalidHash() {
        List<String> ingredients = List.of(RandomDataUtils.invalidIngredientsHash());

        Response response = orderClient.createOrderWithAuth(accessToken, ingredients);

        assertEquals(500, response.getStatusCode());
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    @Description("Проверяю, что API возвращает ошибку при пустом списке ингредиентов и без авторизации")
    public void createOrderWithoutAuthNoIngredients() {
        List<String> ingredients = List.of();

        Response response = orderClient.createOrderWithoutAuth(ingredients);

        assertEquals(400, response.getStatusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals("Ingredient ids must be provided", response.jsonPath().getString("message"));
    }
}