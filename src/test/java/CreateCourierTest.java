import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import static io.restassured.RestAssured.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static resttests.ScooterRegisterCourier.registerNewCourierAndReturnLoginPassword;

public class CreateCourierTest {
    private String id;
    private String login;
    private String password;

    public void setId(String id) {
        this.id = id;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Before
    public void setUp() {
        setId("");
        setLogin("");
        setPassword("");
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @After
    public void deleteData() {
        if ((!Objects.equals(login, "")) && (!Objects.equals(password, ""))) {
            String registerRequestBody = "{\"login\":\"" + login + "\","
                    + "\"password\":\"" + password + "\"}";
            Response response = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(registerRequestBody)
                    .when()
                    .post("/api/v1/courier/login");
            setId(Integer.toString(response.then().extract().path("id")));

            given()
                    .delete("/api/v1/courier/" + id);
            System.out.println("kek " + id + " delete");
        }
    }

    @Test
    public void createCourierPositive() {

        String courierLogin = RandomStringUtils.randomAlphabetic(10);
        String courierPassword = RandomStringUtils.randomAlphabetic(10);
        String courierFirstName = RandomStringUtils.randomAlphabetic(10);
        setLogin(courierLogin);
        setPassword(courierPassword);
        String registerRequestBody = "{\"login\":\"" + courierLogin + "\","
                + "\"password\":\"" + courierPassword + "\","
                + "\"firstName\":\"" + courierFirstName + "\"}";

        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().body("ok",equalTo(true))
                .and()
                .statusCode(201);
    }

    @Test
    public void createCourierWithExistingLogin() {

        ArrayList<String> loginPass = registerNewCourierAndReturnLoginPassword();
        String registerRequestBody = "{\"login\":\"" + loginPass.get(0) + "\","
                + "\"password\":\"" + loginPass.get(1) + "\","
                + "\"firstName\":\"" + "name" + "\"}";

        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post("/api/v1/courier");

        response.then().assertThat().body("message",equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Test
    public void createCourierWithoutPassword() {
        String courierLogin = RandomStringUtils.randomAlphabetic(10);
        String courierFirstName = RandomStringUtils.randomAlphabetic(10);
        String registerRequestBody = "{\"login\":\"" + courierLogin + "\","
                + "\"firstName\":\"" + courierFirstName + "\"}";

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    public void createCourierWithoutLogin() {
        String courierPassword = RandomStringUtils.randomAlphabetic(10);
        String courierFirstName = RandomStringUtils.randomAlphabetic(10);
        String registerRequestBody = "{\"password\":\"" + courierPassword + "\","
                + "\"firstName\":\"" + courierFirstName + "\"}";

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
}
