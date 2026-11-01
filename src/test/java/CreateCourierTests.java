import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Map;
import support.Courier;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CreateCourierTests {


  boolean isCourierCreated = false;
  String login = "test";
  String password = "test1234";

  @BeforeAll
  static void setUp() {
    RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
  }

  @Test
  public void createNewCourierAndCheckResponse() {
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
    response.then()
        .body("", equalTo(Map.of("ok", true)));

  }

  @Test
  public void createNewCourierWithoutPasswordAndCheckStatusCode() {
    login = Courier.generateRandomLogin12();
    String json =
        "{\"login\": \"" + login + "\", \"firstName\": \"Bob\"}";
    Response response = given()
        .header("Content-type", "application/json")
        .body(json)
        .when()
        .post("/api/v1/courier");
    response.then().statusCode(400);
  }

  @Test
  public void createNewCourierWithoutLoginAndCheckStatusCode() {

    String json =
        "{\"password\": \"" + password
            + "\", \"firstName\": \"Bob\"}";
    Response response = given()
        .header("Content-type", "application/json")
        .body(json)
        .when()
        .post("/api/v1/courier");
    response.then().statusCode(400);

  }

  @Test
  public void createNewCourierWithoutFirstNameAndCheckStatusCode() {
    login = Courier.generateRandomLogin12();
    String json =
        "{\"login\": \"" + login + "\", \"password\": \"" + password
            + "\"}";
    Response response = given()
        .header("Content-type", "application/json")
        .body(json)
        .when()
        .post("/api/v1/courier");
    response.then().statusCode(400);

  }

  @Test
  public void createTwoCouriersWithEqualsLoginsAndCheckingResponse() {
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
    response = given()
        .header("Content-type", "application/json")
        .body(json)
        .when()
        .post("/api/v1/courier");
    response.then().statusCode(409);
  }


  @AfterEach
  public void returnBack() {
    if (isCourierCreated == true) {
      String json =
          "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
      Response response = given()
          .header("Content-type", "application/json")
          .body(json)
          .post("/api/v1/courier/login");
      int id = response.jsonPath().getInt("id");
      given()
          .delete("/api/v1/courier/" + id)
          .then()
          .statusCode(200);
      isCourierCreated = false;
    }
  }
}
