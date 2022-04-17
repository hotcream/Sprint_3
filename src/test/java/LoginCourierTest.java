import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import static io.restassured.RestAssured.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static resttests.ScooterRegisterCourier.registerNewCourierAndReturnLoginPassword;

public class LoginCourierTest {

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        id = "";
    }

    @After
    public void deleteData() {
        if (!Objects.equals(id, "")) {
            given()
                    .delete("/api/v1/courier/" + id);
            //System.out.println("kek " + id + " delete");
        }
    }

    @Test
    public void loginCourier() {
        ArrayList<String> loginPass = registerNewCourierAndReturnLoginPassword();
        String registerRequestBody = "{\"login\":\"" + loginPass.get(0) + "\","
                + "\"password\":\"" + loginPass.get(1) + "\"}";

        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("id",notNullValue())
                .and()
                .statusCode(200);

        setId(Integer.toString(response.then().extract().path("id")));
    }

    @Test
    public void authorizationCourierWithoutPassword() {
        ArrayList<String> loginPass = registerNewCourierAndReturnLoginPassword();
        String registerRequestBody = "{\"login\":\"" + loginPass.get(0) + "\"}";

        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message",equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    public void authorizationCourierWithoutLogin() {
        ArrayList<String> loginPass = registerNewCourierAndReturnLoginPassword();
        String registerRequestBody = "{\"password\":\"" + loginPass.get(1) + "\"}";

        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message",equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    public void authorizationCourierWithIncorrectLogin() {
        String courierLogin = RandomStringUtils.randomAlphabetic(10);
        String courierPassword = RandomStringUtils.randomAlphabetic(10);
        String registerRequestBody = "{\"login\":\"" + courierLogin + "\","
                + "\"password\":\"" + courierPassword + "\"}";

        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message",equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    public void authorizationCourierWithIncorrectPassword() {
        ArrayList<String> loginPass = registerNewCourierAndReturnLoginPassword();
        String password = "0000";
        String registerRequestBody = "{\"login\":\"" + loginPass.get(0) + "\","
                + "\"password\":\"" + password + "\"}";

        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message",equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

}
