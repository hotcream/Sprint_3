package util;

import api.model.CreateOrder;
import api.model.LoginCourier;
import api.model.RegisterCourier;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang3.RandomUtils.nextBoolean;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class DataGenerator {
    public static RegisterCourier generateRegisterCourier() {
        return new RegisterCourier(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
    }

    public static CreateOrder generateCreateOrder() {
        var data = new CreateOrder();
        data.setFirstName(randomAlphabetic(10));
        data.setLastName(randomAlphabetic(10));
        data.setAddress(randomAlphabetic(10) + ", " + randomNumeric(1, 3));
        data.setMetroStation(nextInt(1, 10));
        data.setPhone("+7 " + randomNumeric(3) + " " + randomNumeric(3) + " " + randomNumeric(2) + " " + randomNumeric(2));
        data.setRentTime(nextInt(1, 5));
        data.setDeliveryDate(LocalDate.now().plusDays(nextInt(1, 5)).format(DateTimeFormatter.ISO_LOCAL_DATE));
        data.setComment(randomAlphabetic(10));
        if (nextBoolean()) {
            data.getColor().add("BLACK");
        }
        if (nextBoolean()) {
            data.getColor().add("GREY");
        }
        return data;
    }

    public static LoginCourier generateLoginCourier() {
        return new LoginCourier(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
    }
}