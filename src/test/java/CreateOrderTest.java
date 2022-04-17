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

public class CreateOrderTest {

}