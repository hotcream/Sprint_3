import api.client.OrderService;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import util.DataGenerator;
import io.qameta.allure.junit4.DisplayName;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(JUnitParamsRunner.class)
public class CreateOrderTest {

    @Parameters(method = "getColorParams")
    @Test
    @DisplayName("Создание заказа")
    public void shouldCreateOrderWithVariousColors(List<String> color) {
        var data = DataGenerator.generateCreateOrder();
        data.setColor(color);
        OrderService.create(data)
                .then().assertThat()
                .statusCode(201)
                .body("track", notNullValue());
    }

    public static List<List<String>> getColorParams() {
        return List.of(
                List.of("BLACK"),
                List.of("GREY"),
                List.of("BLACK", "GREY"),
                List.of()
        );
    }
}