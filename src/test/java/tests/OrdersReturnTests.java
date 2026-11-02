package tests;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;

import client.OrderClient;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Test;

public class OrdersReturnTests {

  @Test
  public void requestOrdersAndCheckResponse() {
    Response response = OrderClient.ordersRequest();
    response.then().statusCode(200);
    response.then()
        .body("orders", instanceOf(List.class))
        .body("orders", not(empty()));

  }
}
