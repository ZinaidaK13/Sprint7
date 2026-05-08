import data.CourierData;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import io.qameta.allure.junit4.DisplayName;
import request.CreateCourierRequest;
import request.LoginCourierRequest;
import steps.CourierSteps;

public class LoginCourierTest extends BaseAPITest {

    @Before
    public void CourierCreation() {
        CreateCourierRequest request = new CreateCourierRequest(CourierData.COURIER_LOGIN,
                                                                CourierData.COURIER_PASSWORD,
                                                                CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(request);
        CourierSteps.verifyCreateCourierSuccess(response);
    }


    @Test
    @DisplayName("Авторизация курьера с валидными данными")
    @Description("POST /api/v1/courier/login — проверка успешного входа. Ожидается HTTP-статус 200 и наличие идентификатора (id) в теле ответа")
    public void testLoginCourierWithValidCredentials () {
        LoginCourierRequest request = new LoginCourierRequest(CourierData.COURIER_LOGIN,
                                                              CourierData.COURIER_PASSWORD);
        Response response = CourierSteps.sendLoginRequest(request);
        CourierSteps.verifyLoginSuccess(response);

    }

     @Test
    @DisplayName("Авторизация курьера с неверным логином")
    @Description("POST /api/v1/courier/login — проверка входа с невалидными данными. Ожидается HTTP-статус 404 и сообщение об ошибке")
        public void testLoginCourierWithNonExistentLoginReturns404()  {
        LoginCourierRequest request = new LoginCourierRequest(CourierData.COURIER_GUEST_LOGIN,
                                                              CourierData.COURIER_PASSWORD);
        Response response = CourierSteps.sendLoginRequest(request);
        CourierSteps.verifyLoginAccountNotFound(response);

    }
    @Test
    @DisplayName("Авторизация курьера с неверным паролем")
    @Description("POST /api/v1/courier/login — проверка входа с невалидными данными. Ожидается HTTP-статус 404 и сообщение об ошибке")
      public void testLoginCourierWithWrongPasswordReturns404()  {
        LoginCourierRequest request = new LoginCourierRequest(CourierData.COURIER_LOGIN,
                                                              CourierData.COURIER_GUEST_PASSWORD);
        Response response = CourierSteps.sendLoginRequest(request);
        CourierSteps.verifyLoginAccountNotFound(response);

         }

    @Test
    @DisplayName("Авторизация курьера с пустым логином")
    @Description("POST /api/v1/courier/login — проверка входа с невалидными данными. Ожидается HTTP-статус 400 и сообщение об ошибке")
      public void testLoginCourierWithEmptyFieldsReturn400()  {
        LoginCourierRequest request = new LoginCourierRequest(CourierData.EMPTY_LOGIN,
                                                              CourierData.COURIER_PASSWORD);
        Response response = CourierSteps.sendLoginRequest(request);
        CourierSteps.verifyLoginInsufficientCredentials(response);

    }
    @Test
    @DisplayName("Авторизация курьера с пустым паролем")
    @Description("POST /api/v1/courier/login — проверка входа с невалидными данными. Ожидается HTTP-статус 400 и сообщение об ошибке")
        public void testPasswordCourierWithEmptyFieldsReturn400()  {
        LoginCourierRequest request = new LoginCourierRequest(CourierData.COURIER_LOGIN,
                                                              CourierData.EMPTY_PASSWORD);
        Response response = CourierSteps.sendLoginRequest(request);
        CourierSteps.verifyLoginInsufficientCredentials(response);

    }
    @Test
    @DisplayName("Авторизация несуществующего пользователя")
    @Description("POST /api/v1/courier/login — проверка входа с логином, которого нет в системе. Ожидается HTTP-статус 404 и сообщение об ошибке")
        public void testLoginCourierWithNonExistentLogin_returns404()  {
        LoginCourierRequest request = new LoginCourierRequest(CourierData.COURIER_GUEST_LOGIN,
                                                              CourierData.COURIER_PASSWORD);
        Response response = CourierSteps.sendLoginRequest(request);
        CourierSteps.verifyLoginAccountNotFound(response);

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
