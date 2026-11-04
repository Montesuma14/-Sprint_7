package tests;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import steps.OrderSteps;
import support.Order;
import support.OrderTrack;

public class CreateOrderTests {

  int track = 0;

  static Stream<Arguments> orderData() {
    return Stream.of(
        Arguments.of("В color указано Black", new Order(
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
        Arguments.of("В color указано Grey", new Order(
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
        Arguments.of("В color указано Black и Grey", new Order(
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
        Arguments.of("Без передачи color", new Order(
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

  @DisplayName("Создание заказа с валидными данными")
  @ParameterizedTest(name = "{0}")
  @MethodSource("orderData")
  public void createNewOrderAndCheckResponse(String scenario, Order order) {
    Allure.description("Проверка успешного создания заказа с валидными данными: " + scenario);
    Response response = OrderSteps.sendPostRequestOrders(order);
    OrderSteps.compareRequestStatusCode(response,201);
    OrderSteps.compareRequestTrackInMessageText(response);
    track = response.jsonPath().getInt("track");
  }

  @AfterEach
  public void tearDown() {
    if (track != 0) {
      Allure.getLifecycle().updateFixture(
          fixture -> fixture.setName("Очистка тестовых данных")
      );
      Response response = OrderSteps.sendPutRequestOrdersCancel(new OrderTrack(track));
      OrderSteps.compareRequestStatusCode(response, 200);
      track = 0;
    }
  }
}
