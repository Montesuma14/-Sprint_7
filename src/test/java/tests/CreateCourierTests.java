package tests;

import client.CourierClient;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import support.Courier;

public class CreateCourierTests {


  boolean isCourierCreated = false;
  String login = "test";
  String password = "test1234";

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

  @Test
  public void createNewCourierWithoutPasswordAndCheckStatusCode() {
    login = Courier.generateRandomLogin12();
    Courier courier = new Courier();
    courier.setLogin(login);
    courier.setFirstName("Bob");
    Response response = CourierClient.loginCourierRequest(courier);
    response.then().statusCode(400);
  }

  @Test
  public void createNewCourierWithoutLoginAndCheckStatusCode() {
    Courier courier = new Courier();
    courier.setPassword(password);
    courier.setFirstName("Bob");
    Response response = CourierClient.loginCourierRequest(courier);
    response.then().statusCode(400);

  }

  @Test
  public void createNewCourierWithoutFirstNameAndCheckStatusCode() {
    login = Courier.generateRandomLogin12();
    Courier courier = new Courier(login, password);
    Response response = CourierClient.loginCourierRequest(courier);
    response.then().statusCode(400);

  }

  @Test
  public void createTwoCouriersWithEqualsLoginsAndCheckingResponse() {
    login = Courier.generateRandomLogin12();
    Courier courier = new Courier(login, password, "Bob");

    Response response = CourierClient.loginCourierRequest(courier);

    response.then().statusCode(201);
    isCourierCreated = true;
    response = CourierClient.loginCourierRequest(courier);
    response.then().statusCode(409);
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
