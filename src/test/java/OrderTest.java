import data.CourierData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.qameta.allure.junit4.DisplayName;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTest extends BaseAPITest{
    private final OrderRequest orderRequest;

    public OrderTest(OrderRequest orderRequest) {
        this.orderRequest = orderRequest;
    }
    @Parameterized.Parameters(name = "Цвета: {0}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {
                {new OrderRequest("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2026-05-06", "Saske, come back to Konoha", Arrays.asList("black"))},
                {new OrderRequest("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2026-05-06", "Saske, come back to Konoha", Arrays.asList("grey"))},
                {new OrderRequest("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2026-05-06", "Saske, come back to Konoha", Arrays.asList("grey", "black"))},
                {new OrderRequest("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2026-05-06", "Saske, come back to Konoha",  Arrays.asList())},
        });
    }

    @Test
    @DisplayName("Создание заказа")
    public void  testCreateOrderSuccessfully(){
        given()
                .header("Content-type", "application/json")
                .body(orderRequest)
                .when()
                .post(CourierData.ORDER_CREATE_ENDPOINT )
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }

}
