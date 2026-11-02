package tests;

import static org.hamcrest.Matchers.instanceOf;

import client.CourierClient;
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

  @Test
  public void loginNewCourierAndCheckResponse() {

    Response response = CourierClient.loginCourierRequest(new Courier(login, password));
    response.then().statusCode(200);
    response.then()
        .body("id", instanceOf(Integer.class));
  }

  @Test
  public void loginNewCourierWithoutPasswordAndCheckStatusCode() {
    Courier courier = new Courier();
    courier.setLogin(login);
    Response response = CourierClient.loginCourierRequest(courier);
    response.then().statusCode(400);
  }

  @Test
  public void loginNewCourierWithoutLoginAndCheckStatusCode() {
    Courier courier = new Courier();
    courier.setPassword(password);
    Response response = CourierClient.loginCourierRequest(courier);
    response.then().statusCode(400);
  }

  @Test
  public void loginNewCourierWithWrongPasswordAndCheckStatusCode() {
    Response response = CourierClient.loginCourierRequest(new Courier(login, password + password));
    response.then().statusCode(404);
  }

  @Test
  public void loginNewCourierWithWrongLoginAndCheckStatusCode() {

    Response response = CourierClient.loginCourierRequest(new Courier(login + "test", password));
    response.then().statusCode(404);
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
