package ru.practikum.kristinabogatova.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.practikum.kristinabogatova.Endpoints;
import ru.practikum.kristinabogatova.TokenUtils;
import ru.practikum.kristinabogatova.model.OrderRequest;

import java.util.List;

/**
 * Клиент для создания заказов
 */
public class OrderClient {

    public OrderClient() {
        RestAssured.baseURI = Endpoints.BASE_URL;
    }

    public Response createOrderWithAuth(String accessToken, List<String> ingredientHashes) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", TokenUtils.normalizeToken(accessToken))
                .body(new OrderRequest(ingredientHashes))
                .post(Endpoints.ORDERS_PATH)
                .andReturn();
    }

    public Response createOrderWithoutAuth(List<String> ingredientHashes) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new OrderRequest(ingredientHashes))
                .post(Endpoints.ORDERS_PATH)
                .andReturn();
    }
}