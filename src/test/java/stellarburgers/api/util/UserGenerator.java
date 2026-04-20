package stellarburgers.api.util;

import stellarburgers.api.model.TestUser;

import java.util.UUID;

public final class UserGenerator {

    private UserGenerator() {
    }

    public static TestUser createRandomUser() {
        String value = UUID.randomUUID().toString().replace("-", "");
        return new TestUser(
                "qa-" + value.substring(0, 12) + "@example.com",
                "Password123",
                "User" + value.substring(12, 20)
        );
    }
}
