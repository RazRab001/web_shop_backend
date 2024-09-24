package project.onlineshop.domain.user;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.onlineshop.domain.model.Address;
import project.onlineshop.domain.model.User;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/test-data/cleanup.sql")
@Sql("/test-data/base-data.sql")
public class UserIntegrationTest {
    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    public void testGetUserById(){
        when()
                .get("/user/5fdba127-ab33-4881-bcf8-096e210fe7c9")
        .then()
                .statusCode(200)
                .body("email", is("Ivo@gmail.com"))
                .body("password", is("ivo"))
                .body("phone", is("420987333011"));
    }

    @Test
    public void testGetUserById_NotFound(){
        when()
                .get("/user/5fuua127-ab33-4881-bcf8-096uuu0fe7c9")
        .then()
                .statusCode(400);
    }

    @Test
    public void testCreateUser(){
        var user = new User("test1@gmail.com", "testtest");

        var id = given()
                .contentType("application/json")
                .body(user)
                .when()
                .post("/user")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        when()
                .get("/user/" + id)
                .then()
                .statusCode(200)
                .body("email", is("test1@gmail.com"))
                .body("password", is("testtest"));
        when()
                .get("cart/" + id)
                .then()
                .statusCode(200);
    }


    @Test
    public void testCreateUser_WrongEmail(){
        var user = new User("test1gmail.com", "testtest");

        given()
                .contentType("application/json")
                .body(user)
                .when()
                .post("/user")
                .then()
                .statusCode(400);
    }

    @Test
    public void testCreateUser_WrongPhone(){
        var user = new User("test1@gmail.com", "testtest", "4788");

        given()
                .contentType("application/json")
                .body(user)
                .when()
                .post("/user")
                .then()
                .statusCode(400);
    }

    @Test
    public void testAddAddressToUser(){
        var address = new Address("TestCountry", "TestCity", "TestStreet", 1111);

        given()
                .contentType("application/json")
                .body(address)
                .when()
                .post("/user/fffd85db-55c5-4620-b7eb-73191a43533e")
                .then()
                .statusCode(200)
                .body("id", is("fffd85db-55c5-4620-b7eb-73191a43533e"))
                .body("addresses.find { it.country == 'TestCountry' && it.city == 'TestCity' && it.street == 'TestStreet' && it.home == 1111 }", notNullValue());
    }

    @Test
    public void testAddAddressToUserTwice(){
        var address = new Address("TestCountry", "TestCity", "TestStreet", 1111);

        given()
                .contentType("application/json")
                .body(address)
                .when()
                .post("/user/fffd85db-55c5-4620-b7eb-73191a43533e")
                .then()
                .statusCode(200)
                .body("id", is("fffd85db-55c5-4620-b7eb-73191a43533e"))
                .body("addresses.find { it.country == 'TestCountry' && it.city == 'TestCity' && it.street == 'TestStreet' && it.home == 1111 }", notNullValue())
                .body("addresses.size()", is(2));
    }

    @Test
    public void testUpdateUser(){
        var user = new User("test1@gmail.com", "testtest");

        given()
                .contentType("application/json")
                .body(user)
                .when()
                .put("/user/5fdba127-ab33-4881-bcf8-096e210fe7c9")
                .then()
                .statusCode(200);

        when()
                .get("/user/5fdba127-ab33-4881-bcf8-096e210fe7c9")
                .then()
                .statusCode(200)
                .body("email", is("test1@gmail.com"))
                .body("password", is("testtest"));
        when()
                .get("cart/5fdba127-ab33-4881-bcf8-096e210fe7c9")
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdateUser_BadRequest(){
        var user = new User("test1@gmail.com", "testtest");

        given()
                .contentType("application/json")
                .body(user)
                .when()
                .put("/user/5fdba127-abu3-4881-bcf8-096euuufe7c9")
                .then()
                .statusCode(400);
    }

    @Test
    public void testDeleteUser(){
        when()
                .delete("/user/5fdba127-ab33-4881-bcf8-096e210fe7c9")
                .then()
                .statusCode(200);
        when()
                .get("/user/5fdba127-ab33-4881-bcf8-096e210fe7c9")
                .then()
                .statusCode(400);
        when()
                .get("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9")
                .then()
                .statusCode(400);

    }
}
