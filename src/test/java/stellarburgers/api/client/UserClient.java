package stellarburgers.api.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import stellarburgers.api.Endpoints;
import stellarburgers.api.model.TestUser;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserClient extends BaseClient {

    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .create();

    @Step("Создать пользователя")
    public Response createUser(TestUser user) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", user.getEmail());
        payload.put("password", user.getPassword());
        payload.put("name", user.getName());

        return requestSpec()
                .body(GSON.toJson(payload))
                .post(Endpoints.REGISTER);
    }

    @Step("Создать пользователя без поля {fieldName}")
    public Response createUserWithoutField(TestUser user, String fieldName) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", user.getEmail());
        payload.put("password", user.getPassword());
        payload.put("name", user.getName());
        payload.remove(fieldName);

        return requestSpec()
                .body(GSON.toJson(payload))
                .post(Endpoints.REGISTER);
    }

    @Step("Авторизовать пользователя")
    public Response login(TestUser user) {
        return login(user.getEmail(), user.getPassword());
    }

    @Step("Авторизовать пользователя с произвольными данными")
    public Response login(String email, String password) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", email);
        payload.put("password", password);

        return requestSpec()
                .body(GSON.toJson(payload))
                .post(Endpoints.LOGIN);
    }

    @Step("Удалить пользователя")
    public Response deleteUser(String accessToken) {
        return requestSpec()
                .header("Authorization", accessToken)
                .delete(Endpoints.USER);
    }
}
