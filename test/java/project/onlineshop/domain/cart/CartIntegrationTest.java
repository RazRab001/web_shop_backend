package project.onlineshop.domain.cart;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/test-data/cleanup.sql")
@Sql("/test-data/base-data.sql")
public class CartIntegrationTest {
    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    public void testGetCartByOwnerId(){
        when()
                .get("/cart/fffd85db-55c5-4620-b7eb-73191a43533e")
                .then()
                .statusCode(200)
                .body("id", is(200))
                .body("owner", is("fffd85db-55c5-4620-b7eb-73191a43533e"));
    }

    @Test
    public void testGetCartByOwnerId_WrongId(){
        when()
                .get("/cart/uuud85db-5uc5-4620-b7eb-731uua43533e")
                .then()
                .statusCode(400);
    }

    @Test
    public void testAddNewItemInCart(){
        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12")
                .then()
                .statusCode(200)
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(3))
                .body("cartItems.find { it.id == 12 && it.count == 1 }", notNullValue());
    }

    @Test
    public void testAddSameItemInCartTwice(){
        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12")
                .then()
                .statusCode(200)
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(3))
                .body("cartItems.find { it.id == 12 && it.count == 1 }", notNullValue());

        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12")
                .then()
                .statusCode(200)
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(3))
                .body("cartItems.find { it.id == 12 && it.count == 2 }", notNullValue());
    }

    @Test
    public void testAddItemInCart_WrongItem(){
        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/9999")
                .then()
                .statusCode(400);
    }

    @Test
    public void changeItemCountInCart(){
        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12")
                .then()
                .statusCode(200)
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(3))
                .body("cartItems.find { it.id == 12 && it.count == 1 }", notNullValue());

        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12/8")
                .then()
                .statusCode(200)
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(3))
                .body("cartItems.find { it.id == 12 && it.count == 8 }", notNullValue());
    }

    @Test
    public void changeItemCountInCart_ItemNotInCart(){
        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12/8")
                .then()
                .statusCode(200)
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(2))
                .body("cartItems.find { it.id == 12 && it.count == 8 }", nullValue());
    }

    @Test
    public void changeItemCountInCart_WrongItem(){
        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/9999/8")
                .then()
                .statusCode(400);
    }

    @Test
    public void changeItemCountInCart_WrongCount(){
        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12/-8")
                .then()
                .statusCode(400);
    }

    @Test
    public void deleteItemInCart(){
        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12")
                .then()
                .statusCode(200)
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(3))
                .body("cartItems.find { it.id == 12 && it.count == 1 }", notNullValue());

        when()
                .delete("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12")
                .then()
                .statusCode(200)
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(2))
                .body("cartItems.find { it.id == 12 }",nullValue());
    }

    @Test
    public void deleteItemInCart_ItemNotInCart(){
        when()
                .delete("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12")
                .then()
                .statusCode(200)
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(2))
                .body("cartItems.find { it.id == 12 }",nullValue());
    }

    @Test
    public void deleteItemCountInCart_WrongItem(){
        when()
                .delete("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/9999")
                .then()
                .statusCode(200);
    }

    @Test
    public void devastateCart(){
        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9")
                .then()
                .statusCode(200);
        when()
                .get("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9")
                .then()
                .statusCode(200)
                .body("id", is(201))
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(0));
    }

    @Test
    public void testCheckItemsInCartBeforeOrder_NormalState(){
        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12")
                .then()
                .statusCode(200)
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(3))
                .body("cartItems.find { it.id == 12 && it.count == 1 }", notNullValue());

        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12/8")
                .then()
                .statusCode(200)
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(3))
                .body("cartItems.find { it.id == 12 && it.count == 8 }", notNullValue());

        when()
                .post("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9")
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    public void testCheckItemsInCartBeforeOrder_ProblemState(){
        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12")
                .then()
                .statusCode(200)
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(3))
                .body("cartItems.find { it.id == 12 && it.count == 1 }", notNullValue());

        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12/8000")
                .then()
                .statusCode(200)
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(3))
                .body("cartItems.find { it.id == 12 }", notNullValue());

        when()
                .post("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9")
                .then()
                .statusCode(200)
                .body(notNullValue());
    }
}
