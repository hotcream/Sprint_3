import api.client.CourierService;
import api.model.RegisterCourier;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.DataGenerator;
import io.qameta.allure.junit4.DisplayName;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTest {

    RegisterCourier courier;
    CourierService courierService = new CourierService();

    @Before
    public void before() {
        courier = DataGenerator.generateRegisterCourier();
        courierService.register(courier)
                .then().assertThat()
                .statusCode(201);
    }


    @After
    public void after() {
        int id = courierService.login(courier.getLogin(), courier.getPassword())
                .then().assertThat()
                .statusCode(200)
                .extract().path("id");
        courierService.delete(id)
                .then().assertThat()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация курьера")
    public void shouldCourierLogin() {
        courierService.login(courier.getLogin(), courier.getPassword())
                .then().assertThat()
                .statusCode(200)
                .body("id", any(Integer.class));
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    public void shouldNotCourierLoginWithoutLogin() {
        courierService.login(null, courier.getPassword())
                .then().assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    public void shouldNotCourierLoginWithoutPassword() {
        courierService.login(courier.getLogin(), null)
                .then().assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера без пароля и логина")
    public void shouldNotCourierLoginWithoutLoginAndPassword() {
        courierService.login(null, null)
                .then().assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера с неправильным паролем")
    public void shouldNotCourierLoginWithIncorrectPassword() {
        courierService.login(courier.getLogin(), RandomStringUtils.randomAlphabetic(10))
                .then().assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация курьера с неправильным логином")
    public void shouldNotCourierLoginWithIncorrectLogin() {
        courierService.login(RandomStringUtils.randomAlphabetic(10), courier.getPassword())
                .then().assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация незарегистрированного курьера")
    public void shouldNotUnregisteredCourierLogin() {
        courierService.login(DataGenerator.generateLoginCourier())
                .then().assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}