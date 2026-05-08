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
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_CONFLICT;

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
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Step("Проверить ответ: статус 409")
    public static void verifyCreateCourierConflict(Response response) {
        response.then()
                .statusCode(SC_CONFLICT)
                .body("message", IsEqual.equalTo("Этот логин уже используется. Попробуйте другой."))
                .log().all();
    }

    @Step("Проверить ответ: статус 400")
    public static void verifyCreateCourierBadRequest(Response response) {
        response.then()
                .statusCode(SC_BAD_REQUEST)
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
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Step("Проверить ошибку 404: учетная запись не найдена ")
    public static void verifyLoginAccountNotFound(Response response) {
        response.then()
                .log().all()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Проверить ошибку 400: Недостаточно данных для входа")
    public static void verifyLoginInsufficientCredentials(Response response) {
        response.then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Авторизовать курьера для очистки данных")
    public static Response loginForCleanup(String login, String password) {
        CreateCourierRequest request = new CreateCourierRequest(login, password, null);

        return given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post(CourierData.COURIER_LOGIN_ENDPOINT)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Step("Извлечь ID курьера из ответа")
    public static int extractCourierId(Response response) {
        return response.jsonPath().getInt("id");
    }

    @Step("Удалить курьера по ID: {0}")
    public static void deleteCourierById(int id) {
        given()
                .pathParam("id", id)
                .when()
                .log().all()
                .delete(CourierData.COURIER_DELETE_ENDPOINT)
                .then()
                .statusCode(SC_OK)
                .body("ok", equalTo(true));
    }

}
