package project.onlineshop.domain.order;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.onlineshop.domain.model.Address;
import project.onlineshop.domain.model.Order;
import project.onlineshop.domain.model.User;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/test-data/cleanup.sql")
@Sql("/test-data/base-data.sql")
public class OrderIntegrationTest {
    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }
    @Test
    public void testGetOrderById(){
        when()
                .get("/order/10")
                .then()
                .statusCode(200)
                .body("id", is(10))
                .body("sum", is(80f))
                .body("password", is("H6ni8O"))
                .body("cartItems.find { it.id == 10 }", notNullValue())
                .body("cartItems.find { it.id == 11 }", notNullValue());
    }

    @Test
    public void testGetOrderById_BadRequest(){
        when()
                .get("/order/9999")
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetOrderByPassword() {
        // Get the order ID by password
        var orderId = given()
                .when()
                .get("/order/pass/H6ni8O")
                .then()
                .statusCode(200)
                .extract()
                .as(Long.class);

        // Verify the order details using the retrieved order ID
        given()
                .pathParam("id", orderId)
                .when()
                .get("/order/{id}")
                .then()
                .statusCode(200)
                .body("id", is(orderId.intValue())) // Ensure the returned ID matches
                .body("sum", is(80f)) // Ensure the correct sum
                .body("password", is("H6ni8O")) // Ensure the correct password
                .body("cartItems.find { it.id == 10 }", notNullValue()) // Ensure cart item with ID 10 exists
                .body("cartItems.find { it.id == 11 }", notNullValue()); // Ensure cart item with ID 11 exists
    }


    @Test
    public void testGetOrderByPassword_BadRequest(){
        when()
                .get("/order/pass/Hahaha")
                .then()
                .statusCode(400);
    }

    @Test
    public void testCreateOrder(){
        var address = new Address("TestCountry", "TestCity", "TestStreet", 1111);

        given()
                .contentType("application/json")
                .body(address)
                .when()
                .post("/order/fffd85db-55c5-4620-b7eb-73191a43533e")
                .then()
                .statusCode(200);

        when()
                .get("/cart/fffd85db-55c5-4620-b7eb-73191a43533e")
                .then()
                .statusCode(200)
                .body("cartItems", is(empty()));
    }

    @Test
    public void testCreateOrder_WrongUserId(){
        var address = new Address("TestCountry", "TestCity", "TestStreet", 1111);

        given()
                .contentType("application/json")
                .body(address)
                .when()
                .post("/order/uuud85db-55c5-4620-b7eb-73191a43533e")
                .then()
                .statusCode(400);
    }

    @Test
    public void testDeleteOrder(){
        when()
                .delete("/order/10")
                .then()
                .statusCode(200);
        when()
                .get("/order/10")
                .then()
                .statusCode(400);
    }
}
