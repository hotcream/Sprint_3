import api.client.OrderService;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import java.util.List;

import static org.hamcrest.Matchers.isA;

public class GetOrderListTest {

    @Test
    @DisplayName("Получение списка заказов")
    public void shouldReturnNotNullOrdersList() {
        OrderService.getList()
                .then().assertThat()
                .statusCode(200)
                .body("orders", isA(List.class));
    }

}