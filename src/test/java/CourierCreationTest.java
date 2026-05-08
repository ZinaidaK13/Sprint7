import io.qameta.allure.Description;
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
    @DisplayName("Создание курьера: успешный сценарий")
    @Description("POST /api/v1/courier — создание нового курьера с валидными данными")
    public void testCreateCourierSuccessfully() {
    CreateCourierRequest request = new CreateCourierRequest(CourierData.COURIER_LOGIN,
                                                            CourierData.COURIER_PASSWORD,
                                                            CourierData.COURIER_FIRST_NAME);
     Response response = CourierSteps.sendCreateCourierRequest(request);
     CourierSteps.verifyCreateCourierSuccess(response);
    }

    @Test
    @DisplayName("Создание курьера: неуспешный сценарий")
    @Description("POST /api/v1/courier — создание двух одинаковых курьеров")
    public void testCreateCourierWithDuplicateLogin() {
        CreateCourierRequest firstRequest = new CreateCourierRequest(CourierData.COURIER_LOGIN,
                                                                     CourierData.COURIER_PASSWORD,
                                                                     CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(firstRequest);
        CourierSteps.verifyCreateCourierSuccess(response);


        CreateCourierRequest Request = new CreateCourierRequest(CourierData.COURIER_LOGIN,
                                                                CourierData.COURIER_PASSWORD,
                                                                CourierData.COURIER_FIRST_NAME);
        Response duplicateresponse = CourierSteps.sendCreateCourierRequest(Request);
        CourierSteps.verifyCreateCourierConflict(duplicateresponse);

    }

    @Test
    @DisplayName("Создание курьера: неуспешный сценарий")
    @Description("POST /api/v1/courier — Ошибка 400, если не передать поле логин")
    public void testCreateCourierWithMissingRequiredFieldRreturn400 () {
        CreateCourierRequest request = new CreateCourierRequest(null,
                                                                CourierData.COURIER_PASSWORD,
                                                                CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(request);
        CourierSteps.verifyCreateCourierBadRequest(response);

    }

    @Test
    @DisplayName("Создание курьера: неуспешный сценарий")
    @Description("POST /api/v1/courier — Ошибка 400, если не передать поле пароль")
    public void testCreateCourierFieldPasswordNull() {
        CreateCourierRequest request = new CreateCourierRequest(CourierData.COURIER_LOGIN,
                                                       null,
                                                                CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(request);
        CourierSteps.verifyCreateCourierBadRequest(response);

    }


    @Test
    @DisplayName("Создание курьера: неуспешный сценарий")
    @Description("POST /api/v1/courier — Ошибка 409")
    public void testCreateCourierReturn409() {
        CreateCourierRequest request = new CreateCourierRequest(CourierData.COURIER_LOGIN,
                                                                CourierData.COURIER_PASSWORD,
                                                                CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(request);
        CourierSteps.verifyCreateCourierSuccess(response);

        CreateCourierRequest Request = new CreateCourierRequest(CourierData.COURIER_LOGIN,
                                                                CourierData.COURIER_GUEST_PASSWORD,
                                                                CourierData.COURIER_GUEST_FIRST_NAME);
        Response duplicateresponse = CourierSteps.sendCreateCourierRequest(Request);
        CourierSteps.verifyCreateCourierConflict(duplicateresponse);

            }


    @Test
    @DisplayName("Создание курьера: неуспешный сценарий")
    @Description("POST /api/v1/courier — Ошибка 409б если создать пользователя с логином, который уже есть")
    public void testCreateCourierWithDuplicateLogiRreturn409() {
        CreateCourierRequest request = new CreateCourierRequest(CourierData.COURIER_LOGIN,
                                                                CourierData.COURIER_PASSWORD,
                                                                CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(request);
        CourierSteps.verifyCreateCourierSuccess(response);

        CreateCourierRequest Request = new CreateCourierRequest(CourierData.COURIER_LOGIN,
                                                                CourierData.COURIER_GUEST_PASSWORD,
                                                                CourierData.COURIER_GUEST_FIRST_NAME);
        Response duplicateresponse = CourierSteps.sendCreateCourierRequest(Request);
        CourierSteps.verifyCreateCourierConflict(duplicateresponse);
    }
    @After
    public void cleanUpCourier () {
        Response loginResponse = CourierSteps.loginForCleanup(
                CourierData.COURIER_LOGIN,
                CourierData.COURIER_PASSWORD);
              if (loginResponse.statusCode() == 200) {
        int courierId = CourierSteps.extractCourierId(loginResponse);
        CourierSteps.deleteCourierById(courierId);
    }
}
    }

