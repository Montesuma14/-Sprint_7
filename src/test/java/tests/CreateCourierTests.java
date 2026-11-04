package tests;


import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.CourierSteps;
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
    Response response = CourierSteps.sendPostRequestCourier(courier);
    CourierSteps.compareRequestStatusCode(response, 201);
    isCourierCreated = true;
    CourierSteps.compareRequestOKMessageText(response);
  }

  @DisplayName("Создание курьера без пароля")
  @Description("Проверка  создания курьера без пароля и ответа запроса")
  @Test
  public void createNewCourierWithoutPasswordAndCheckStatusCode() {
    login = Courier.generateRandomLogin12();
    Courier courier = new Courier();
    courier.setLogin(login);
    courier.setFirstName("Bob");
    Response response = CourierSteps.sendPostRequestCourier(courier);
    CourierSteps.compareRequestStatusCode(response, 400);
    CourierSteps.compareRequestErrorMessageText(response,
        "Недостаточно данных для создания учетной записи");
  }

  @DisplayName("Создание курьера без логина")
  @Description("Проверка  создания курьера без логина и ответа запроса")
  @Test
  public void createNewCourierWithoutLoginAndCheckStatusCode() {
    Courier courier = new Courier();
    courier.setPassword(password);
    courier.setFirstName("Bob");
    Response response = CourierSteps.sendPostRequestCourier(courier);
    CourierSteps.compareRequestStatusCode(response, 400);
    CourierSteps.compareRequestErrorMessageText(response,
        "Недостаточно данных для создания учетной записи");

  }

  @DisplayName("Создание курьера без имени")
  @Description("Проверка  создания курьера без имени  и ответа запроса")
  @Test
  public void createNewCourierWithoutFirstNameAndCheckStatusCode() {
    login = Courier.generateRandomLogin12();
    Courier courier = new Courier(login, password);
    Response response = CourierSteps.sendPostRequestCourier(courier);
    CourierSteps.compareRequestStatusCode(response, 201);
    isCourierCreated = true;
    CourierSteps.compareRequestOKMessageText(response);

  }

  @DisplayName("Создание курьера с повторяющимся логином")
  @Description("Проверка  создания курьера с логином уже созданного курьера и ответа запроса")
  @Test
  public void createTwoCouriersWithEqualsLoginsAndCheckingResponse() {
    login = Courier.generateRandomLogin12();
    Courier courier = new Courier(login, password, "Bob");
    Response response = CourierSteps.sendPostRequestCourier(courier);
    CourierSteps.compareRequestStatusCode(response, 201);
    isCourierCreated = true;
    response = CourierSteps.sendPostRequestCourier(courier);
    CourierSteps.compareRequestStatusCode(response, 409);
    CourierSteps.compareRequestErrorMessageText(response, "Этот логин уже используется");
  }


  @AfterEach
  public void tearDown() {
    Allure.getLifecycle().updateFixture(
        fixture -> fixture.setName("Очистка тестовых данных")
    );
    if (isCourierCreated) {
      Response response = CourierSteps.sendPostRequestCourierLogin(new Courier(login, password));
      int id = response.jsonPath().getInt("id");
      Response response1 = CourierSteps.sendPostRequestCourierDelete(id);
      CourierSteps.compareRequestStatusCode(response1, 200);
      isCourierCreated = false;
    }
  }
}
