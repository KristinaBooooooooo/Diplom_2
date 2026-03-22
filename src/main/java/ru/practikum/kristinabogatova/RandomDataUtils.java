package ru.practikum.kristinabogatova;

import java.util.UUID;

/**
 * Утилиты для генерации случайных данных
 */
public class RandomDataUtils {

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
        return "invalid_hash_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

}