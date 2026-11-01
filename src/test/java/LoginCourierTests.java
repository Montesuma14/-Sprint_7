import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.instanceOf;

import support.Courier;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginCourierTests {

  String login = "test";
  String password = "test1234";

  boolean isCourierCreated = false;

  @BeforeAll
  static void setUp() {
    RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
  }

  @BeforeEach
  public void createCourier() {
    login = Courier.generateRandomLogin12();
    String json =
        "{\"login\": \"" + login + "\", \"password\": \"" + password
            + "\", \"firstName\": \"Bob\"}";
    Response response = given()
        .header("Content-type", "application/json")
        .body(json)
        .when()
        .post("/api/v1/courier");
    response.then().statusCode(201);
    isCourierCreated = true;
  }

  @Test
  public void loginNewCourierAndCheckResponse() {
    String json =
        "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
    Response response = given()
        .header("Content-type", "application/json")
        .body(json)
        .post("/api/v1/courier/login");
    response.then().statusCode(200);
    response.then()
        .body("id", instanceOf(Integer.class));
  }

  @Test
  public void loginNewCourierWithoutPasswordAndCheckStatusCode() {
    String json =
        "{\"login\": \"" + login + "\"}";
    Response response = given()
        .header("Content-type", "application/json")
        .body(json)
        .post("/api/v1/courier/login");
    response.then().statusCode(400);
  }

  @Test
  public void loginNewCourierWithoutLoginAndCheckStatusCode() {
    String json =
        "{\"password\": \"" + password + "\"}";
    Response response = given()
        .header("Content-type", "application/json")
        .body(json)
        .post("/api/v1/courier/login");
    response.then().statusCode(400);
  }

  @Test
  public void loginNewCourierWithWrongPasswordAndCheckStatusCode() {
    String json =
        "{\"login\": \"" + login + "\", \"password\": \"" + password + "test" + "\"}";
    Response response = given()
        .header("Content-type", "application/json")
        .body(json)
        .post("/api/v1/courier/login");
    response.then().statusCode(404);
  }

  @Test
  public void loginNewCourierWithWrongLoginAndCheckStatusCode() {
    String json =
        "{\"login\": \"" + login + "test" + "\", \"password\": \"" + password + "\"}";
    Response response = given()
        .header("Content-type", "application/json")
        .body(json)
        .post("/api/v1/courier/login");
    response.then().statusCode(404);
  }

  @AfterEach
  public void returnBack() {
    if (isCourierCreated) {
      String json =
          "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
      Response response = given()
          .header("Content-type", "application/json")
          .body(json)
          .post("/api/v1/courier/login");
      response.then().statusCode(200);
      int id = response.jsonPath().getInt("id");
      given()
          .delete("/api/v1/courier/" + id)
          .then()
          .statusCode(200);
      isCourierCreated = false;
    }
  }
}
