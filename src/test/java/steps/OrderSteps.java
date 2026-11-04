package steps;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import client.OrderClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;
import support.Order;
import support.OrderTrack;

public class OrderSteps extends BaseSteps {
  @Step("Отправка запроса POST /api/v1/orders")
  public static Response sendPostRequestOrders(Order order) {
    return OrderClient.createOrderRequest(order);
  }

  @Step("Отправка запроса PUT /api/v1/orders/cancel")
  public static Response sendPutRequestOrdersCancel(OrderTrack orderTrack) {
    return OrderClient.cancelOrderRequest(orderTrack);
  }

  @Step("Отправка запроса GET /api/v1/orders")
  public static Response sendGetRequestOrders() {
    return OrderClient.ordersRequest();
  }

  @Step("Проверка возвращения track в ответе запроса")
  public static void compareRequestTrackInMessageText(Response response){
    response.then()
        .body("track", instanceOf(Integer.class));
  }

  @Step("Проверка возвращения заказов в ответе запроса orders")
  public static void compareRequestOrders(Response response){
    response.then()
        .body("orders", instanceOf(List.class))
        .body("orders", not(empty()));
  }
}
