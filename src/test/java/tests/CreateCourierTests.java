package tests;

import client.CourierClient;
import static org.hamcrest.Matchers.equalTo;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.Courier;

public class CreateCourierTests {

  boolean isCourierCreated = false;
  String login = "test";
  String password = "test1234";

  @DisplayName("Создание курьера c валидными данными")
  @Description("Проверка успешного создания курьера с валидными данными")
  @Test
  public void createNewCourierAndCheckResponse() {
    login = Courier.generateRandomLogin12();
    Courier courier = new Courier(login, password, "Bob");
    Response response = CourierClient.createCourierRequest(courier);
    response.then().statusCode(201);
    isCourierCreated = true;
    response.then()
        .body("", equalTo(Map.of("ok", true)));

  }

  @DisplayName("Создание курьера без пароля")
  @Description("Проверка  создания курьера без пароля и ответа запроса")
  @Test
  public void createNewCourierWithoutPasswordAndCheckStatusCode() {
    login = Courier.generateRandomLogin12();
    Courier courier = new Courier();
    courier.setLogin(login);
    courier.setFirstName("Bob");
    Response response = CourierClient.createCourierRequest(courier);
    response.then()
        .statusCode(400)
        .body("message", equalTo("Недостаточно данных для создания учетной записи"));

  }

  @DisplayName("Создание курьера без логина")
  @Description("Проверка  создания курьера без логина и ответа запроса")
  @Test
  public void createNewCourierWithoutLoginAndCheckStatusCode() {
    Courier courier = new Courier();
    courier.setPassword(password);
    courier.setFirstName("Bob");
    Response response = CourierClient.createCourierRequest(courier);
    response.then()
        .statusCode(400)
        .body("message", equalTo("Недостаточно данных для создания учетной записи"));

  }

  @DisplayName("Создание курьера без имени")
  @Description("Проверка  создания курьера без имени  и ответа запроса")
  @Test
  public void createNewCourierWithoutFirstNameAndCheckStatusCode() {
    login = Courier.generateRandomLogin12();
    Courier courier = new Courier(login, password);
    Response response = CourierClient.createCourierRequest(courier);
    response.then()
        .statusCode(201);
    response.then()
        .body("", equalTo(Map.of("ok", true)));

  }

  @DisplayName("Создание курьера с повторяющимся логином")
  @Description("Проверка  создания курьера с логином уже созданного курьера и ответа запроса")
  @Test
  public void createTwoCouriersWithEqualsLoginsAndCheckingResponse() {
    login = Courier.generateRandomLogin12();
    Courier courier = new Courier(login, password, "Bob");

    Response response = CourierClient.createCourierRequest(courier);

    response.then().statusCode(201);
    isCourierCreated = true;
    response = CourierClient.createCourierRequest(courier);
    response.then()
        .statusCode(409)
        .body("message", equalTo("Этот логин уже используется"));
  }

  @AfterEach
  public void returnBack() {
    if (isCourierCreated) {
      Response response = CourierClient.loginCourierRequest(new Courier(login, password));
      int id = response.jsonPath().getInt("id");
      CourierClient.deleteCourierRequest(id)
          .then()
          .statusCode(200);
      isCourierCreated = false;
    }
  }
}
