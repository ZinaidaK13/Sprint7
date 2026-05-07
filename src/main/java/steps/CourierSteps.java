package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.core.IsEqual;
import request.CreateCourierRequest;
import data.CourierData;
import request.LoginCourierRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class CourierSteps {

    @Step("Отправить POST-запрос на создание курьера")
    public static Response sendCreateCourierRequest(CreateCourierRequest request) {
        return given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post(CourierData.COURIER_CREATE_ENDPOINT);
    }

    @Step("Проверить успешный ответ: статус 201, поле ok = true")
    public static void verifyCreateCourierSuccess(Response response) {
        response.then()
                .log().all()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Проверить ответ: статус 409")
    public static void verifyCreateCourierConflict(Response response) {
        response.then()
                .statusCode(409)
                .body("message", IsEqual.equalTo("Этот логин уже используется. Попробуйте другой."))
                .log().all();
    }

    @Step("Проверить ответ: статус 400")
    public static void verifyCreateCourierBadRequest(Response response) {
        response.then()
                .statusCode(400)
                .body("message", IsEqual.equalTo("Недостаточно данных для создания учетной записи"))
                .log().all();
    }

    @Step("Отправить запрос на авторизацию курьера")
    public static Response sendLoginRequest(LoginCourierRequest request) {
        return given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post(CourierData.COURIER_LOGIN_ENDPOINT);
    }

    @Step("Проверить успешную авторизацию: статус 200 и наличие ID в ответе")
    public static void verifyLoginSuccess(Response response) {
        response.then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Step("Проверить ошибку 404: учетная запись не найдена ")
    public static void verifyLoginAccountNotFound(Response response) {
        response.then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Проверить ошибку 400: Недостаточно данных для входа")
    public static void verifyLoginInsufficientCredentials(Response response) {
        response.then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

}


