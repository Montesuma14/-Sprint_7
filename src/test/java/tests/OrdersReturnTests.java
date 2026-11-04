package tests;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.OrderSteps;

public class OrdersReturnTests {

  @DisplayName("Получение списка заказов")
  @Description("Проверка ручки получения полного списка всех заказов")
  @Test
  public void requestOrdersAndCheckResponse() {
    Response response = OrderSteps.sendGetRequestOrders();
    OrderSteps.compareRequestStatusCode(response, 200);
    OrderSteps.compareRequestOrders(response);
  }
}
