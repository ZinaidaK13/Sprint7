import data.CourierData;
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
        CreateCourierRequest request = new CreateCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_PASSWORD,CourierData.COURIER_FIRST_NAME);
        Response response = CourierSteps.sendCreateCourierRequest(request);
        CourierSteps.verifyCreateCourierSuccess(response);
    }


    @Test
    @DisplayName("Курьер может авторизоваться")
    public void testLoginCourierWithValidCredentials () {
        LoginCourierRequest request = new LoginCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_PASSWORD);
        Response response = CourierSteps.sendLoginRequest(request);
        CourierSteps.verifyLoginSuccess(response);

    }

    @Test
    @DisplayName("Обязательные поля для авторизации")
    public void testLoginCourierWithValidCredentialsReturn200 () {
        LoginCourierRequest request = new LoginCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_PASSWORD);
        Response response = CourierSteps.sendLoginRequest(request);
        CourierSteps.verifyLoginSuccess(response);
    }
    @Test
    @DisplayName("Ошибка при неверном логине")
    public void testLoginCourierWithNonExistentLoginReturns404()  {
        LoginCourierRequest request = new LoginCourierRequest(CourierData.COURIER_GUEST_LOGIN, CourierData.COURIER_PASSWORD);
        Response response = CourierSteps.sendLoginRequest(request);
        CourierSteps.verifyLoginAccountNotFound(response);

    }
    @Test
    @DisplayName("Ошибка при неверном пароле")
    public void testLoginCourierWithWrongPasswordReturns404()  {
        LoginCourierRequest request = new LoginCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_GUEST_PASSWORD);
        Response response = CourierSteps.sendLoginRequest(request);
        CourierSteps.verifyLoginAccountNotFound(response);

         }

    @Test
    @DisplayName("Ошибка если нет логина")
    public void testLoginCourierWithEmptyFieldsReturn400()  {
        LoginCourierRequest request = new LoginCourierRequest(CourierData.EMPTY_LOGIN, CourierData.COURIER_PASSWORD);
        Response response = CourierSteps.sendLoginRequest(request);
        CourierSteps.verifyLoginInsufficientCredentials(response);

    }
    @Test
    @DisplayName("Ошибка если нет пароля")
    public void testPasswordCourierWithEmptyFieldsReturn400()  {
        LoginCourierRequest request = new LoginCourierRequest(CourierData.COURIER_LOGIN, CourierData.EMPTY_PASSWORD);
        Response response = CourierSteps.sendLoginRequest(request);
        CourierSteps.verifyLoginInsufficientCredentials(response);

    }
    @Test
    @DisplayName("Несущетсующий пользователь")
    public void testLoginCourierWithNonExistentLogin_returns404()  {
        LoginCourierRequest request = new LoginCourierRequest(CourierData.COURIER_GUEST_LOGIN, CourierData.COURIER_PASSWORD);
        Response response = CourierSteps.sendLoginRequest(request);
        CourierSteps.verifyLoginAccountNotFound(response);

    }
    @Test
    @DisplayName("Успешный запрос возвращает id")
    public void testCreateCourierReturnsIdOnSuccess () {
        LoginCourierRequest request = new LoginCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_PASSWORD);
        Response response = CourierSteps.sendLoginRequest(request);
        CourierSteps.verifyLoginSuccess(response);
    }

    @After
    public void cleanUpCourier () {
        LoginCourierRequest loginRequest = new LoginCourierRequest(CourierData.COURIER_LOGIN, CourierData.COURIER_PASSWORD);
        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(loginRequest)
                .when()
                .post(CourierData.COURIER_LOGIN_ENDPOINT)
                .then()
                .extract()
                .response();

        int statusCode = loginResponse.getStatusCode();

        if (statusCode == 200) {
            int id = loginResponse.jsonPath().getInt("id");
            given()
                    .pathParam("id", id)
                    .when()
                    .delete(CourierData.COURIER_DELETE_ENDPOINT)
                    .then()
                    .statusCode(200)
                    .body("ok", equalTo(true));


        }
    }
}
