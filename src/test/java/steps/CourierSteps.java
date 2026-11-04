package steps;

import static org.hamcrest.Matchers.instanceOf;

import client.CourierClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import support.Courier;

public class CourierSteps extends BaseSteps {
  @Step("Отправка запроса POST /api/v1/courier")
  public static Response sendPostRequestCourier(Courier courier) {
    return CourierClient.createCourierRequest(courier);
  }

  @Step("Отправка запроса POST /api/v1/courier/login")
  public static Response sendPostRequestCourierLogin(Courier courier) {
    return CourierClient.loginCourierRequest(courier);
  }

  @Step("Отправка запроса DELETE /api/v1/courier/delete для курьера с id {0}")
  public static Response sendPostRequestCourierDelete(int id) {
    return CourierClient.deleteCourierRequest(id);
  }

  @Step("Проверка возвращения id в ответе запроса")
  public static void compareRequestIdInMessageText(Response response) {
    response.then()
        .body("id", instanceOf(Integer.class));
  }
}
