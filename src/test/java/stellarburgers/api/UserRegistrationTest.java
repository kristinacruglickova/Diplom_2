package stellarburgers.api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;
import stellarburgers.api.util.ApiMessages;
import stellarburgers.api.util.UserGenerator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserRegistrationTest extends BaseApiTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка, что API создаёт нового уникального пользователя и возвращает токены.")
    public void createUniqueUserReturnsSuccess() {
        createdUser = UserGenerator.createRandomUser();

        Response response = userClient.createUser(createdUser);
        accessToken = response.path("accessToken");

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(createdUser.getEmail()))
                .body("user.name", equalTo(createdUser.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Проверка, что API отклоняет повторную регистрацию пользователя с теми же данными.")
    public void createExistingUserReturnsConflict() {
        createdUser = UserGenerator.createRandomUser();
        Response firstResponse = userClient.createUser(createdUser);
        accessToken = firstResponse.path("accessToken");

        userClient.createUser(createdUser)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo(ApiMessages.USER_ALREADY_EXISTS));
    }
}
