package tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import steps.CourierSteps;
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
    Response response = CourierSteps.sendPostRequestCourier(new Courier(login, password, "Bob"));
    CourierSteps.compareRequestStatusCode(response, 201);
    isCourierCreated = true;
  }

  @DisplayName("Логин курьера валидными данными")
  @Description("Проверка  логина курьера с валидными данными  и ответа запроса")
  @Test
  public void loginNewCourierAndCheckResponse() {
    Response response = CourierSteps.sendPostRequestCourierLogin(new Courier(login, password));
    CourierSteps.compareRequestStatusCode(response, 200);
    CourierSteps.compareRequestIdInMessageText(response);
  }

  @DisplayName("Логин курьера без пароля")
  @Description("Проверка  логина курьера без пароля   и ответа запроса")
  @Test
  public void loginNewCourierWithoutPasswordAndCheckStatusCode() {
    Courier courier = new Courier();
    courier.setLogin(login);
    Response response = CourierSteps.sendPostRequestCourierLogin(courier);
    CourierSteps.compareRequestStatusCode(response, 400);
    CourierSteps.compareRequestErrorMessageText(response, "Недостаточно данных для входа");
  }

  @DisplayName("Логин курьера без поля логин")
  @Description("Проверка логина курьера без поля логин   и ответа запроса")
  @Test
  public void loginNewCourierWithoutLoginAndCheckStatusCode() {
    Courier courier = new Courier();
    courier.setPassword(password);
    Response response = CourierSteps.sendPostRequestCourierLogin(courier);
    CourierSteps.compareRequestStatusCode(response, 400);
    CourierSteps.compareRequestErrorMessageText(response, "Недостаточно данных для входа");
  }

  @DisplayName("Логин курьера с некорректным паролем")
  @Description("Проверка  логина курьера с некорректным паролем   и ответа запроса")
  @Test
  public void loginNewCourierWithWrongPasswordAndCheckStatusCode() {
    Response response = CourierSteps.sendPostRequestCourierLogin(
        new Courier(login, password + password));
    CourierSteps.compareRequestStatusCode(response, 404);
    CourierSteps.compareRequestErrorMessageText(response, "Учетная запись не найдена");
  }

  @DisplayName("Логин курьера с некоректным логином")
  @Description("Проверка  логина курьера с некорректным логином и ответа запроса")
  @Test
  public void loginNewCourierWithWrongLoginAndCheckStatusCode() {
    Response response = CourierSteps.sendPostRequestCourierLogin(
        new Courier(login + "test", password));
    CourierSteps.compareRequestStatusCode(response, 404);
    CourierSteps.compareRequestErrorMessageText(response, "Учетная запись не найдена");
  }

  @AfterEach
  public void tearDown() {
    if (isCourierCreated) {
      Allure.getLifecycle().updateFixture(
          fixture -> fixture.setName("Очистка тестовых данных")
      );
      Response response =
          CourierSteps.sendPostRequestCourierLogin(new Courier(login, password));
      CourierSteps.compareRequestStatusCode(response,200);
      int id = response.jsonPath().getInt("id");
      CourierSteps.sendPostRequestCourierDelete(id);
      isCourierCreated = false;
    }
  }
}
