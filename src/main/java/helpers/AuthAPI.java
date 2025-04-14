package helpers;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class AuthAPI {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";

    public static Response registerUser(String email, String password, String name) {
        return given()
                .header("Content-type", "application/json")
                .body(String.format("{\"email\": \"%s\", \"password\": \"%s\", \"name\": \"%s\"}",
                        email, password, name))
                .when()
                .post(BASE_URL + "/auth/register");
    }

    public static void deleteUser(String accessToken) {
        if (accessToken != null && !accessToken.isEmpty()) {
            given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete(BASE_URL + "/auth/user")
                    .then()
                    .statusCode(202);
        }
    }

    public static String getAccessToken(String email, String password) {
        return given()
                .header("Content-type", "application/json")
                .body(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password))
                .when()
                .post(BASE_URL + "/auth/login")
                .then()
                .extract()
                .path("accessToken");
    }

    public static String loginAndGetToken(String email, String password) {
        return given()
                .header("Content-type", "application/json")
                .body(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password))
                .when()
                .post(BASE_URL + "/auth/login")
                .then()
                .extract()
                .path("accessToken");
    }
}
