import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import io.qameta.allure.junit4.DisplayName;
import data.CourierData;
import request.CreateCourierRequest;
import steps.CourierSteps;

public class CourierCreationTest extends BaseAPITest {

    @Test
    @DisplayName("Курьера можно создать")
    public void testCreateCourierSuccessfully() {
    CreateCourierRequest request = new CreateCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_PASSWORD,CourierData.COURIER_FIRST_NAME);
     Response response = CourierSteps.sendCreateCourierRequest(request);
     CourierSteps.verifyCreateCourierSuccess(response);
    }


    @Test
    @DisplayName("Нельзя создать двух одинковых курьеров")
    public void testCreateCourierWithDuplicateLogin() {
        CreateCourierRequest firstRequest = new CreateCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_PASSWORD, CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(firstRequest);
        CourierSteps.verifyCreateCourierSuccess(response);


        CreateCourierRequest Request = new CreateCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_PASSWORD, CourierData.COURIER_FIRST_NAME);
        Response duplicateresponse = CourierSteps.sendCreateCourierRequest(Request);
        CourierSteps.verifyCreateCourierConflict(duplicateresponse);

    }

    @Test
    @DisplayName("Нет поля логин")
    public void testCreateCourierFieldLoginNull() {
        CreateCourierRequest request = new CreateCourierRequest(null, CourierData.COURIER_PASSWORD,CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(request);
        CourierSteps.verifyCreateCourierBadRequest(response);

    }

    @Test
    @DisplayName("Нет поля пароль")
    public void testCreateCourierFieldPasswordNull() {
        CreateCourierRequest request = new CreateCourierRequest(CourierData.COURIER_LOGIN, null,CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(request);
        CourierSteps.verifyCreateCourierBadRequest(response);

    }

    @Test
    @DisplayName("Код ответа 201")
    public void testCreateCourierReturn201() {
        CreateCourierRequest request = new CreateCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_PASSWORD,CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(request);
        CourierSteps.verifyCreateCourierSuccess(response);
    }

    @Test
    @DisplayName("Код ответа 400")
    public void testCreateCourierReturn400() {
        CreateCourierRequest request = new CreateCourierRequest(CourierData.COURIER_LOGIN, null,CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(request);
        CourierSteps.verifyCreateCourierBadRequest(response);
    }

    @Test
    @DisplayName("Код ответа 409")
    public void testCreateCourierReturn409() {
        CreateCourierRequest request = new CreateCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_PASSWORD,CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(request);
        CourierSteps.verifyCreateCourierSuccess(response);

        CreateCourierRequest Request = new CreateCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_GUEST_PASSWORD, CourierData.COURIER_GUEST_FIRST_NAME);
        Response duplicateresponse = CourierSteps.sendCreateCourierRequest(Request);
        CourierSteps.verifyCreateCourierConflict(duplicateresponse);

            }

    @Test
    @DisplayName("Успешный запрос возвращает ок: true")
    public void testSuccessfulRequestReturnsOkTrue() {
        CreateCourierRequest request = new CreateCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_PASSWORD,CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(request);
        CourierSteps.verifyCreateCourierSuccess(response);

    }

    @Test
    @DisplayName("Ошибка 400, если не передать поле логин")
    public void testCreateCourierWithMissingRequiredFieldRreturn400 () {
        CreateCourierRequest request = new CreateCourierRequest(null, CourierData.COURIER_PASSWORD,CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(request);
        CourierSteps.verifyCreateCourierBadRequest(response);

    }
    @Test
    @DisplayName("Ошибка если создать пользователя с логином, который уже есть")
    public void testCreateCourierWithDuplicateLogiRreturn409() {
        CreateCourierRequest request = new CreateCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_PASSWORD,CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(request);
        CourierSteps.verifyCreateCourierSuccess(response);

        CreateCourierRequest Request = new CreateCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_GUEST_PASSWORD, CourierData.COURIER_GUEST_FIRST_NAME);
        Response duplicateresponse = CourierSteps.sendCreateCourierRequest(Request);
        CourierSteps.verifyCreateCourierConflict(duplicateresponse);
    }
    @After
    public void cleanUpCourier () {
        CreateCourierRequest loginRequest = new CreateCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_PASSWORD, null);
        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(loginRequest)
                .when()
                .post(CourierData.COURIER_LOGIN_ENDPOINT)
                .then()
                .log().all()
                .extract()
                .response();

        int statusCode = loginResponse.getStatusCode();

        if (statusCode == 200) {
            int id = loginResponse.jsonPath().getInt("id");
            given()
                    .pathParam("id", id)
                    .when()
                    .log().all()
                    .delete(CourierData.COURIER_DELETE_ENDPOINT)
                    .then()
                    .statusCode(200)
                    .body("ok", equalTo(true));


        }
    }
}