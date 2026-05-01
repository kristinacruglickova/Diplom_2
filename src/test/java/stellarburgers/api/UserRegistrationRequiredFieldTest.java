package stellarburgers.api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import stellarburgers.api.util.ApiMessages;
import stellarburgers.api.util.UserGenerator;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class UserRegistrationRequiredFieldTest extends BaseApiTest {

    private final String missedField;

    public UserRegistrationRequiredFieldTest(String missedField) {
        this.missedField = missedField;
    }

    @Parameterized.Parameters(name = "missing {0}")
    public static Object[][] getData() {
        return new Object[][]{
                {"email"},
                {"password"},
                {"name"}
        };
    }

    @Test
    @DisplayName("Создание пользователя без одного из обязательных полей")
    @Description("Проверка, что API возвращает ошибку при отсутствии одного из обязательных полей.")
    public void createUserWithoutRequiredFieldReturnsError() {
        createdUser = UserGenerator.createRandomUser();

        userClient.createUserWithoutField(createdUser, missedField)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo(ApiMessages.REQUIRED_FIELDS_MESSAGE));
    }
}
