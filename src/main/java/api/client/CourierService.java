package api.client;

import api.model.LoginCourier;
import api.model.RegisterCourier;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CourierService {

    public Response register(RegisterCourier registerCourier) {
        return given()
                .contentType(ContentType.JSON)
                .body(registerCourier)
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier");
    }

    public Response login(String login, String password) {
        return login(new LoginCourier(login, password));
    }

    public Response login(LoginCourier loginCourier) {
        return given()
                .contentType(ContentType.JSON)
                .body(loginCourier)
                .post("https://qa-scooter.praktikum-services.ru/api/v1/courier/login");
    }

    public Response delete(int id) {
        return given()
                .contentType(ContentType.JSON)
                .body(Map.of("id", id))
                .delete("https://qa-scooter.praktikum-services.ru/api/v1/courier/{id}", id);
    }

}