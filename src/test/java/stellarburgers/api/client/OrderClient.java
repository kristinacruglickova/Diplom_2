package stellarburgers.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import stellarburgers.api.Endpoints;
import stellarburgers.api.model.CreateOrderRequest;

import java.util.Collections;
import java.util.List;

public class OrderClient extends BaseClient {

    @Step("Получить ингредиенты")
    public Response getIngredients() {
        return requestSpec()
                .get(Endpoints.INGREDIENTS);
    }

    @Step("Создать заказ")
    public Response createOrder(List<String> ingredientIds, String accessToken) {
        List<String> ingredients = ingredientIds == null ? Collections.emptyList() : ingredientIds;
        io.restassured.specification.RequestSpecification specification = requestSpec()
                .body(new CreateOrderRequest(ingredients));

        if (accessToken != null) {
            specification.header("Authorization", accessToken);
        }

        return specification.post(Endpoints.ORDERS);
    }
}
