import io.restassured.response.Response;
import request.CreateCourierRequest;
import data.CourierData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierSteps {

    public static Response sendCreateCourierRequest(CreateCourierRequest request) {
        return given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post(CourierData.COURIER_CREATE_ENDPOINT);
       }


    public static void verifyCreateCourierSuccess(Response response) {
        response.then()
                .log().all()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

     public static int extractCourierId(Response response) {
        return response.jsonPath().getInt("id");
    }
}
