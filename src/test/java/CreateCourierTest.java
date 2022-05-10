import api.client.CourierService;
import api.model.RegisterCourier;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import util.DataGenerator;
import java.util.ArrayList;
import java.util.List;
import io.qameta.allure.junit4.DisplayName;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {
    List<RegisterCourier> createdCouriers = new ArrayList<>();

    private Response register(RegisterCourier data) {
        CourierService courierService = new CourierService();
        var response = courierService.register(data);
        if (response.statusCode() == 201) {
            createdCouriers.add(data);
        }
        return response;
    }

    @After
    public void after() {
        CourierService courierService = new CourierService();
        for (var data : createdCouriers) {
            int id = courierService.login(data.getLogin(), data.getPassword())
                    .then().assertThat()
                    .statusCode(200)
                    .extract().path("id");
            courierService.delete(id)
                    .then().assertThat()
                    .statusCode(200)
                    .body("ok", equalTo(true));
        }
    }

    @Test
    @DisplayName("Создание курьера")
    public void shouldCreateCourier() {
        register(DataGenerator.generateRegisterCourier())
                .then().assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера без имени")
    public void shouldCreateCourierWithoutFirstName() {
        var data = DataGenerator.generateRegisterCourier();
        data.setFirstName(null);

        register(data)
                .then().assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void shouldNotCreateCourierWithoutLogin() {
        var data = DataGenerator.generateRegisterCourier();
        data.setLogin(null);

        register(data)
                .then().assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void shouldNotCreateCourierWithoutPassword() {
        var data = DataGenerator.generateRegisterCourier();
        data.setPassword(null);

        register(data)
                .then().assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    public void shouldNotCreateTwoEqualsCouriers() {
        var data = DataGenerator.generateRegisterCourier();
        register(data)
                .then().assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));
        register(data)
                .then().assertThat()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Создание курьера с существующим логином")
    public void shouldNotCreateCourierWithAlreadyExistingLogin() {
        var data1 = DataGenerator.generateRegisterCourier();
        register(data1)
                .then().assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));


        var data2 = DataGenerator.generateRegisterCourier();
        data2.setLogin(data1.getLogin());
        register(data2)
                .then().assertThat()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));
    }
}