package stellarburgers.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterUserRequest {

    private final String email;
    private final String password;
    private final String name;

    public static RegisterUserRequest from(TestUser user) {
        return new RegisterUserRequest(user.getEmail(), user.getPassword(), user.getName());
    }

    public static RegisterUserRequest withoutField(TestUser user, String fieldName) {
        String email = user.getEmail();
        String password = user.getPassword();
        String name = user.getName();
        switch (fieldName) {
            case "email":
                email = null;
                break;
            case "password":
                password = null;
                break;
            case "name":
                name = null;
                break;
            default:
                throw new IllegalArgumentException("Unknown field: " + fieldName);
        }
        return new RegisterUserRequest(email, password, name);
    }
}
