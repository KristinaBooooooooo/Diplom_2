package ru.practikum.kristinabogatova;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;
import java.util.UUID;

/**
 * Утилиты для генерации случайных данных и получения валидных ингредиентов
 */
public class Utils {

    public static String generateRandomEmail() {
        return "user_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10) + "@example.com";
    }

    public static String generateRandomName() {
        return "Name_" + UUID.randomUUID().toString().substring(0, 5);
    }

    public static String generateRandomPassword() {
        return "Pass_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public static String invalidIngredientsHash() {
        return "invalid_hash_1234567890";
    }

    /**
     * Получаем валидные ингредиенты с сервера
     */
    public static String[] getValidIngredientHashes() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru/api";
        Response resp = RestAssured.given().get("/ingredients").then().extract().response();

        if (resp.statusCode() != 200) return new String[]{invalidIngredientsHash()};

        List<String> ids = resp.jsonPath().getList("data._id", String.class);
        if (ids == null || ids.isEmpty()) return new String[]{invalidIngredientsHash()};

        int take = Math.min(ids.size(), 2);
        return ids.subList(0, take).toArray(new String[0]);
    }

    public static String normalizeToken(String token) {
        return token == null ? null : token.trim();
    }
}