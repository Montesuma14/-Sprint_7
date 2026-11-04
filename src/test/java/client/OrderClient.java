package client;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import support.ApiClient;
import support.Order;
import support.OrderTrack;

public class OrderClient {

  public static Response createOrderRequest(Order order) {
    return given()
        .spec(ApiClient.SPEC)
        .body(order)
        .when()
        .post("/api/v1/orders");
  }

  public static Response cancelOrderRequest(OrderTrack orderTrack) {
    return given()
        .spec(ApiClient.SPEC)
        .body(orderTrack)
        .put("/api/v1/orders/cancel");
  }

  public static Response ordersRequest() {
    return given()
        .spec(ApiClient.SPEC)
        .when()
        .get("/api/v1/orders");
  }
}
