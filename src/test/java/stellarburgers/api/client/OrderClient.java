package stellarburgers.api.client;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import stellarburgers.api.Endpoints;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderClient extends BaseClient {

    private static final Gson GSON = new Gson();

    @Step("Получить ингредиенты")
    public Response getIngredients() {
        return requestSpec()
                .get(Endpoints.INGREDIENTS);
    }

    @Step("Создать заказ")
    public Response createOrder(List<String> ingredientIds, String accessToken) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("ingredients", ingredientIds == null ? Collections.emptyList() : ingredientIds);

        io.restassured.specification.RequestSpecification specification = requestSpec()
                .body(GSON.toJson(payload));

        if (accessToken != null) {
            specification.header("Authorization", accessToken);
        }

        return specification.post(Endpoints.ORDERS);
    }
}
