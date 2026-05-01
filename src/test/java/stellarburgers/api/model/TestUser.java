package stellarburgers.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestUser {

    private final String email;
    private final String password;
    private final String name;
}
