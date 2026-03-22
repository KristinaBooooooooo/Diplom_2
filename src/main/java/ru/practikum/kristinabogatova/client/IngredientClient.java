package ru.practikum.kristinabogatova.client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.practikum.kristinabogatova.Endpoints;

import java.util.List;

/**
 * Клиент получения ингредиентов
 */
public class IngredientClient {

    public IngredientClient() {
        RestAssured.baseURI = Endpoints.BASE_URL;
    }

    @Step("Получаю валидные ингредиенты")
    public List<String> getTwoIngredientHashes() {
        Response resp = RestAssured.given()
                .get(Endpoints.INGREDIENTS_PATH)
                .then()
                .extract()
                .response();
        if (resp.statusCode() != 200) {
            return List.of();
        }
        List<String> ids = resp.jsonPath().getList("data._id", String.class);
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return ids.subList(0, 2);
    }
}