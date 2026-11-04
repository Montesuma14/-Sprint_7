package steps;

import static org.hamcrest.Matchers.equalTo;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.Map;

public class BaseSteps {

  @Step("Проверка кода ответа запроса")
  public static void compareRequestStatusCode(Response response, int messageCode) {
    response.then().statusCode(messageCode);
  }

  @Step("Проверка сообщения об ошибке")
  public static void compareRequestErrorMessageText(Response response, String messageText) {
    response.then()
        .body("message", equalTo(messageText));
  }

  @Step("Проверка успешного ответа сервера")
  public static void compareRequestOKMessageText(Response response) {
    response.then()
        .body("", equalTo(Map.of("ok", true)));
  }
}
