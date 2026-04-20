package stellarburgers.api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import stellarburgers.api.util.ApiMessages;
import stellarburgers.api.util.UserGenerator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest extends BaseApiTest {

    @Test
    @DisplayName("Авторизация существующего пользователя")
    @Description("Проверка, что API позволяет авторизовать ранее созданного пользователя.")
    public void loginWithExistingUserReturnsSuccess() {
        createdUser = UserGenerator.createRandomUser();
        accessToken = userClient.createUser(createdUser).path("accessToken");

        Response response = userClient.login(createdUser);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(createdUser.getEmail()))
                .body("user.name", equalTo(createdUser.getName()));
    }

    @Test
    @DisplayName("Авторизация с некорректными данными")
    @Description("Проверка, что API отклоняет вход с неверным email и паролем.")
    public void loginWithInvalidCredentialsReturnsUnauthorized() {
        userClient.login("wrong-user@example.com", "wrongPassword")
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo(ApiMessages.LOGIN_INCORRECT));
    }
}
