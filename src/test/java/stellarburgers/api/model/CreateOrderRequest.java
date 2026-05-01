package stellarburgers.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreateOrderRequest {

    private final List<String> ingredients;
}
