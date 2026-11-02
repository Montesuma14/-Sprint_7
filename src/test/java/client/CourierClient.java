package client;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import support.ApiClient;
import support.Courier;

public class CourierClient {

  public static Response createCourierRequest(Courier courier) {
    return given()
        .spec(ApiClient.SPEC)
        .body(courier)
        .when()
        .post("/api/v1/courier");
  }
  public static Response loginCourierRequest(Courier courier){
    courier.setFirstName(null);
    return given()
        .spec(ApiClient.SPEC)
        .body(courier)
        .post("/api/v1/courier/login");
  }

  public static Response deleteCourierRequest(int id){
    return given()
        .spec(ApiClient.SPEC)
        .delete("/api/v1/courier/" + id);
  }
}


