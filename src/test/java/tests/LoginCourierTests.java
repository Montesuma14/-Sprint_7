package tests;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import client.CourierClient;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import support.Courier;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginCourierTests {

  String login = "test";
  String password = "test1234";
  boolean isCourierCreated = false;

  @BeforeEach
  public void createCourier() {
    login = Courier.generateRandomLogin12();
    Response response = CourierClient.createCourierRequest(new Courier(login, password, "Bob"));
    response.then().statusCode(201);
    isCourierCreated = true;
  }

  @DisplayName("Логин курьера валидными данными")
  @Description("Проверка  логина курьера с валидными данными  и ответа запроса")
  @Test
  public void loginNewCourierAndCheckResponse() {
    Response response = CourierClient.loginCourierRequest(new Courier(login, password));
    response.then().statusCode(200);
    response.then()
        .body("id", instanceOf(Integer.class));
  }

  @DisplayName("Логин курьера без пароля")
  @Description("Проверка  логина курьера без пароля   и ответа запроса")
  @Test
  public void loginNewCourierWithoutPasswordAndCheckStatusCode() {
    Courier courier = new Courier();
    courier.setLogin(login);
    Response response = CourierClient.loginCourierRequest(courier);
    response.then()
        .statusCode(400)
        .body("message", equalTo("Недостаточно данных для входа"));
  }

  @DisplayName("Логин курьера без поля логин")
  @Description("Проверка логина курьера без поля логин   и ответа запроса")
  @Test
  public void loginNewCourierWithoutLoginAndCheckStatusCode() {
    Courier courier = new Courier();
    courier.setPassword(password);
    Response response = CourierClient.loginCourierRequest(courier);
    response.then()
        .statusCode(400)
        .body("message", equalTo("Недостаточно данных для входа"));
  }

  @DisplayName("Логин курьера с некорректным паролем")
  @Description("Проверка  логина курьера с некорректным паролем   и ответа запроса")
  @Test
  public void loginNewCourierWithWrongPasswordAndCheckStatusCode() {
    Response response = CourierClient.loginCourierRequest(new Courier(login, password + password));
    response.then()
        .statusCode(404)
        .body("message", equalTo("Учетная запись не найдена"));
  }

  @DisplayName("Логин курьера с некоректным логином")
  @Description("Проверка  логина курьера с некорректным логином и ответа запроса")
  @Test
  public void loginNewCourierWithWrongLoginAndCheckStatusCode() {
    Response response = CourierClient.loginCourierRequest(new Courier(login + "test", password));
    response.then()
        .statusCode(404)
        .body("message", equalTo("Учетная запись не найдена"));
  }

  @AfterEach
  public void returnBack() {
    if (isCourierCreated) {
      Response response =
          CourierClient.loginCourierRequest(new Courier(login, password));
      response.then().statusCode(200);
      int id = response.jsonPath().getInt("id");
      CourierClient.deleteCourierRequest(id);
      isCourierCreated = false;
    }
  }
}
