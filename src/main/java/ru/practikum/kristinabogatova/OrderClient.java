package ru.practikum.kristinabogatova;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Клиент для создания заказов
 */
public class OrderClient {

    private final String BASE_URL = "https://stellarburgers.education-services.ru/api";

    public OrderClient() {
        RestAssured.baseURI = BASE_URL;
    }

    public Response createOrderWithAuth(String accessToken, String[] ingredientHashes) {
        Map<String, Object> body = new HashMap<>();
        body.put("ingredients", ingredientHashes);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", Utils.normalizeToken(accessToken))
                .body(body)
                .post("/orders")
                .andReturn();
    }

    public Response createOrderWithoutAuth(String[] ingredientHashes) {
        Map<String, Object> body = new HashMap<>();
        body.put("ingredients", ingredientHashes);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("/orders")
                .andReturn();
    }
}