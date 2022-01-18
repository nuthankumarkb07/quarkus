package com.ge;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class tcpconnectorTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/connector")
          .then()
             .statusCode(200)
             .body(is("Hello RESTEasy"));
    }

}