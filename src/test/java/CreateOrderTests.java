import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.instanceOf;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import support.Order;

public class CreateOrderTests {

  int track = 0;

  @BeforeAll
  static void setUp() {
    RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
  }


  static Stream<Arguments> orderData() {
    return Stream.of(
        Arguments.of(new Order(
            "Naruta",
            "Uchihf",
            "Konoha, 141 apt.",
            4,
            "+7 800 355 15 35",
            2,
            "2021-06-06",
            "Saskeddd, come back to Konoha",
            List.of("BLACK"))
        ),
        Arguments.of(new Order(
            "Narutaaa",
            "Uchihfa",
            "Konoha, 144 apt.",
            6,
            "+7 100 355 35 35",
            1,
            "2022-06-06",
            "Saskfffe, come back to Konoha",
            List.of("GREY"))
        ),
        Arguments.of(new Order(
            "Naruteee",
            "Uchihaaa",
            "Konoha, 148 apt.",
            1,
            "+7 900 355 35 35",
            9,
            "2020-01-06",
            "Saske, coffffme back to Konoha",
            List.of("BLACK", "GREY"))
        ),
        Arguments.of(new Order(
            "Naruto",
            "Uchiha",
            "Konoha, 142 apt.",
            4,
            "+7 800 355 35 35",
            5,
            "2020-06-06",
            "Saske, come back to Konoha",
            null)
        )
    );
  }

  @ParameterizedTest
  @MethodSource("orderData")
  public void createNewOrderAndCheckResponse(Order order) throws Exception {
    Response response = given()
        .header("Content-type", "application/json")
        .body(order)
        .when()
        .post("/api/v1/orders");

    response.then().statusCode(201);
    response.then()
        .body("track", instanceOf(Integer.class));
    track = response.jsonPath().getInt("track");
  }

  @AfterEach
  public void returnBack() {
    if (track != 0) {

      String json =
          "{\"track\": \"" + track + "\"}";

      given()
          .header("Content-type", "application/json")
          .body(json)
          .put("/api/v1/orders/cancel")
          .then()
          .statusCode(200);
      track = 0;
    }
  }
}
