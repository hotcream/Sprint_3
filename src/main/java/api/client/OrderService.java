package api.client;

import api.model.CreateOrder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderService {

    public static Response create(CreateOrder createOrder) {
        return given()
                .contentType(ContentType.JSON)
                .body(createOrder)
                .post("https://qa-scooter.praktikum-services.ru/api/v1/orders");
    }

    public static Response getList() {
        return given()
                .contentType(ContentType.JSON)
                .get("https://qa-scooter.praktikum-services.ru/api/v1/orders");
    }
}