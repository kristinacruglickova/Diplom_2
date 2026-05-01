package stellarburgers.api.client;

import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;
import stellarburgers.api.Endpoints;

import static io.restassured.RestAssured.given;

public abstract class BaseClient {

    static {
        RestAssured.baseURI = Endpoints.BASE_URI;
        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.GSON));
    }

    protected RequestSpecification requestSpec() {
        return given()
                .header("Content-Type", "application/json");
    }
}
