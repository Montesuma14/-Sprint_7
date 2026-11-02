package support;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiClient {
  public static final RequestSpecification SPEC = new RequestSpecBuilder()
      .setBaseUri("https://qa-scooter.praktikum-services.ru")
      .setContentType(ContentType.JSON)
      .build();
}
