package stellarburgers.api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.api.client.OrderClient;
import stellarburgers.api.util.ApiMessages;
import stellarburgers.api.util.UserGenerator;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreationTest extends BaseApiTest {

    private final OrderClient orderClient = new OrderClient();
    private List<String> ingredientIds;

    @Before
    public void setUp() {
        ingredientIds = orderClient.getIngredients()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath()
                .getList("data._id");

        createdUser = UserGenerator.createRandomUser();
        accessToken = userClient.createUser(createdUser).path("accessToken");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка, что авторизованный пользователь может создать заказ.")
    public void createOrderWithAuthorizationReturnsSuccess() {
        orderClient.createOrder(ingredientIds.subList(0, 2), accessToken)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("order.owner.email", equalTo(createdUser.getEmail()));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка, что неавторизованный пользователь может создать заказ.")
    public void createOrderWithoutAuthorizationReturnsSuccess() {
        orderClient.createOrder(ingredientIds.subList(0, 2), null)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Проверка успешного создания заказа с валидными идентификаторами ингредиентов.")
    public void createOrderWithIngredientsReturnsSuccess() {
        orderClient.createOrder(ingredientIds.subList(0, 3), null)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка, что API отклоняет заказ без идентификаторов ингредиентов.")
    public void createOrderWithoutIngredientsReturnsError() {
        orderClient.createOrder(List.of(), null)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo(ApiMessages.INGREDIENTS_REQUIRED));
    }

    @Test
    @DisplayName("Создание заказа с некорректным хешем ингредиентов")
    @Description("Проверка, что API отклоняет заказ с невалидными идентификаторами ингредиентов.")
    public void createOrderWithInvalidIngredientReturnsError() {
        orderClient.createOrder(List.of("invalid_hash"), null)
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("success", equalTo(false))
                .body("message", equalTo(ApiMessages.INGREDIENTS_INCORRECT));
    }
}
