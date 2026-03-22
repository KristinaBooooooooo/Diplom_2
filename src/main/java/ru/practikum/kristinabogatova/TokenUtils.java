package ru.practikum.kristinabogatova;

public class TokenUtils {

    public static String normalizeToken(String token) {
        return token == null ? null : token.trim();
    }

}
