package ru.practikum.kristinabogatova.client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.practikum.kristinabogatova.Endpoints;
import ru.practikum.kristinabogatova.TokenUtils;
import ru.practikum.kristinabogatova.model.LoginUserRequest;
import ru.practikum.kristinabogatova.model.RegisterUserRequest;

/**
 * Клиент для работы с пользовательскими эндпоинтами
 */
public class UserClient {

    public UserClient() {
        RestAssured.baseURI = Endpoints.BASE_URL;
    }

    @Step("Создаю пользователя")
    public Response createUser(String email, String password, String name) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new RegisterUserRequest(email, password, name))
                .post(Endpoints.REGISTER_PATH)
                .andReturn();
    }

    @Step("Авторизую пользователя")
    public Response loginUser(String email, String password) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new LoginUserRequest(email, password))
                .post(Endpoints.LOGIN_PATH)
                .andReturn();
    }

    @Step("Удаляю пользователя")
    public Response deleteUser(String accessToken) {
        if (accessToken == null) return null;

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", TokenUtils.normalizeToken(accessToken))
                .delete(Endpoints.USER_PATH)
                .andReturn();
    }
}