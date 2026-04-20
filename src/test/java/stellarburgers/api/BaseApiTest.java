package stellarburgers.api;

import io.restassured.response.Response;
import org.junit.After;
import stellarburgers.api.client.UserClient;
import stellarburgers.api.model.TestUser;

public abstract class BaseApiTest {

    protected final UserClient userClient = new UserClient();
    protected TestUser createdUser;
    protected String accessToken;

    @After
    public void tearDown() {
        if (accessToken == null && createdUser != null) {
            Response loginResponse = userClient.login(createdUser);
            if (loginResponse.statusCode() == 200) {
                accessToken = loginResponse.path("accessToken");
            }
        }

        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}
