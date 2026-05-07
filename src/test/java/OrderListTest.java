import data.CourierData;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.junit4.DisplayName;

public class OrderListTest extends BaseAPITest {
    public int createOrder(){
        OrderRequest request = new OrderRequest("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2026-05-06", "Saske, come back to Konoha", Arrays.asList("black"));

        Response response =given()
                .header("Content-type", "application/json")
                .body(request)
                .when()
                .post(CourierData.ORDER_CREATE_ENDPOINT)
                .then()
                .statusCode(201)
                .extract()
                .response();
        return response.jsonPath().getInt("track");
    }

    @Test
    @DisplayName("В тело ответа возвращается заказ")
    public void testGetOrderByTrack() {
        int track = createOrder();
        System.out.println(track);
        Response response = given()
                .pathParam("track", track)
                .when()
                .get("/api/v1/orders/track?t={track}")
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertThat(response.jsonPath().get("order"), notNullValue());
    }

}
