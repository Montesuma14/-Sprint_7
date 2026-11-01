import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class OrdersReturnTests {

  @BeforeAll
  static void setUp() {
    RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
  }

  @Test
  public void requestOrdersAndCheckResponse() {
    Response response = given()
        .when()
        .get("/api/v1/orders");
    response.then().statusCode(200);
    response.then()
        .body("orders", instanceOf(List.class))
        .body("orders", not(empty()));

  }
}
