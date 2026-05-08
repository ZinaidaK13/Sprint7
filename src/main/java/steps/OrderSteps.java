package steps;

import data.CourierData;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import request.OrderRequest;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderSteps {
    @Step("Создать заказ с переданными данными")
    public static Response createOrder(OrderRequest request) {
        return given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post(CourierData.ORDER_CREATE_ENDPOINT)
                .then()
                .statusCode(SC_CREATED)
                .extract()
                .response();
    }

    @Step("Получить заказы по треку: {0}")
    public static Response getOrdersByTrack(int track) {
        return given()
                .queryParam("t", track)
                .when()
                .get(CourierData.ORDER_LIST_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    @Step("Проверить, что в ответе присутствует заказ")
    public static void verifyOrderExists(Response response) {
        response.then()
                .statusCode(SC_OK)
                .body("order", notNullValue());
    }
}
