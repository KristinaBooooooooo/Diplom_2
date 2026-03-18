package ru.practikum.kristinabogatova;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Клиент для работы с пользовательскими эндпоинтами
 */
public class UserClient {

    private final String BASE_URL = "https://stellarburgers.education-services.ru/api";

    public UserClient() {
        RestAssured.baseURI = BASE_URL;
    }

    public Response createUser(String email, String password, String name) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        body.put("name", name);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("/auth/register")
                .andReturn();
    }

    public Response loginUser(String email, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("/auth/login")
                .andReturn();
    }

    public Response deleteUser(String accessToken) {
        if (accessToken == null) return null;

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken.trim())
                .delete("/auth/user")
                .andReturn();
    }
}