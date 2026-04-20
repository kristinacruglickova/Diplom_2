package stellarburgers.api.client;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import stellarburgers.api.Endpoints;

import static io.restassured.RestAssured.given;

public abstract class BaseClient {

    static {
        RestAssured.baseURI = Endpoints.BASE_URI;
    }

    protected RequestSpecification requestSpec() {
        return given()
                .header("Content-Type", "application/json");
    }
}
