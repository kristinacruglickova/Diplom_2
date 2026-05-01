package stellarburgers.api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.api.util.ApiMessages;
import stellarburgers.api.util.UserGenerator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest extends BaseApiTest {

    @Before
    public void setUp() {
        createdUser = UserGenerator.createRandomUser();
        accessToken = userClient.createUser(createdUser).path("accessToken");
    }

    @Test
    @DisplayName("Авторизация существующего пользователя")
    @Description("Проверка, что API позволяет авторизовать ранее созданного пользователя.")
    public void loginWithExistingUserReturnsSuccess() {
        Response response = userClient.login(createdUser);

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(createdUser.getEmail()))
                .body("user.name", equalTo(createdUser.getName()));
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Проверка, что API отклоняет вход с неверным email.")
    public void loginWithInvalidEmailReturnsUnauthorized() {
        userClient.login("wrong-user@example.com", createdUser.getPassword())
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo(ApiMessages.LOGIN_INCORRECT));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверка, что API отклоняет вход с неверным паролем.")
    public void loginWithInvalidPasswordReturnsUnauthorized() {
        userClient.login(createdUser.getEmail(), "wrongPassword")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo(ApiMessages.LOGIN_INCORRECT));
    }
}
