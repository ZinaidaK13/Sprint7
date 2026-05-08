import io.restassured.response.Response;
import org.junit.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.junit4.DisplayName;
import request.OrderRequest;
import steps.OrderSteps;

public class OrderListTest extends BaseAPITest {

    @Test
    @DisplayName("В тело ответа возвращается заказ")
    public void testGetOrderByTrack() {
        OrderRequest request = new OrderRequest(
                "Naruto", "Uchiha", "Konoha, 142 apt.", "4",
                "+7 800 355 35 35", 5, "2026-05-06",
                "Saske, come back to Konoha", Arrays.asList("black"));
        Response createResponse = OrderSteps.createOrder(request);
        int track = createResponse.jsonPath().getInt("track");
        Response getOrderResponse = OrderSteps.getOrdersByTrack(track);
        OrderSteps.verifyOrderExists(getOrderResponse);
    }

}
