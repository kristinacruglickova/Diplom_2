package stellarburgers.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import stellarburgers.api.Endpoints;
import stellarburgers.api.model.LoginRequest;
import stellarburgers.api.model.RegisterUserRequest;
import stellarburgers.api.model.TestUser;

public class UserClient extends BaseClient {

    @Step("Создать пользователя")
    public Response createUser(TestUser user) {
        return requestSpec()
                .body(RegisterUserRequest.from(user))
                .post(Endpoints.REGISTER);
    }

    @Step("Создать пользователя без поля {fieldName}")
    public Response createUserWithoutField(TestUser user, String fieldName) {
        return requestSpec()
                .body(RegisterUserRequest.withoutField(user, fieldName))
                .post(Endpoints.REGISTER);
    }

    @Step("Авторизовать пользователя")
    public Response login(TestUser user) {
        return login(new LoginRequest(user.getEmail(), user.getPassword()));
    }

    @Step("Авторизовать пользователя с произвольными данными")
    public Response login(String email, String password) {
        return login(new LoginRequest(email, password));
    }

    @Step("Авторизовать пользователя (тело запроса)")
    public Response login(LoginRequest credentials) {
        return requestSpec()
                .body(credentials)
                .post(Endpoints.LOGIN);
    }

    @Step("Удалить пользователя")
    public Response deleteUser(String accessToken) {
        return requestSpec()
                .header("Authorization", accessToken)
                .delete(Endpoints.USER);
    }
}
