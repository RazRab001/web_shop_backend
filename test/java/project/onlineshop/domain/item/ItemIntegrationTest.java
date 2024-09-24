package project.onlineshop.domain.item;

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
import project.onlineshop.domain.model.Item;
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
public class ItemIntegrationTest {
    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    public void testGetItemById(){
        when()
                .get("/item/10")
                .then()
                .statusCode(200)
                .body("name", is("item-1"))
                .body("id", is(10))
                .body("price", is(20f))
                .body("count", is(120));
    }

    @Test
    public void testGetItemById_WrongId(){
        when()
                .get("/item/9999")
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetAllItems(){
        when()
                .get("/item")
                .then()
                .statusCode(200);
    }

    @Test
    public void testCreateItem(){
        var item = new Item("test", 111.11f, 11);

        var id = given()
                .contentType("application/json")
                .body(item)
                .when()
                .post("/item")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        when()
                .get("/item/" + id)
                .then()
                .statusCode(200)
                .body("name", is("test"))
                .body("id", is(id))
                .body("price", is(111.11f))
                .body("count", is(11));
    }

    @Test
    public void testCreateItem_EmptyName(){
        var item = new Item(null, 111.11f, 11);

        given()
                .contentType("application/json")
                .body(item)
                .when()
                .post("/item")
                .then()
                .statusCode(400);

        item = new Item("", 111.11f, 11);

        given()
                .contentType("application/json")
                .body(item)
                .when()
                .post("/item")
                .then()
                .statusCode(400);
    }

    @Test
    public void testCreateItem_WrongCount(){
        var item = new Item("test", 111.11f, -11);

        given()
                .contentType("application/json")
                .body(item)
                .when()
                .post("/item")
                .then()
                .statusCode(400);

        item = new Item("test", 111.11f, null);

        given()
                .contentType("application/json")
                .body(item)
                .when()
                .post("/item")
                .then()
                .statusCode(400);
    }

    @Test
    public void testCreateItem_WrongPrice(){
        var item = new Item("test", -111.11f, 11);

        given()
                .contentType("application/json")
                .body(item)
                .when()
                .post("/item")
                .then()
                .statusCode(400);

        item = new Item("test", null, 11);

        given()
                .contentType("application/json")
                .body(item)
                .when()
                .post("/item")
                .then()
                .statusCode(400);
    }

    @Test
    public void testUpdateItem(){
        var item = new Item("test", 111.11f, 11);

        given()
                .contentType("application/json")
                .body(item)
                .when()
                .put("/item/10")
                .then()
                .statusCode(200);

        when()
                .get("/item/10")
                .then()
                .statusCode(200)
                .body("name", is("test"))
                .body("id", is(10))
                .body("price", is(111.11f))
                .body("count", is(11));
    }

    @Test
    public void testChangeItemCount(){
        when()
                .put("/item/10/12")
                .then()
                .statusCode(200);

        when()
                .get("/item/10")
                .then()
                .statusCode(200)
                .body("count", is(12));
    }

    @Test
    public void testChangeItemCount_WrongCount(){
        when()
                .put("/item/10/-12")
                .then()
                .statusCode(400);

        when()
                .get("/item/10")
                .then()
                .statusCode(200)
                .body("count", is(120));
    }

    @Test
    public void testDeleteItem(){
        when()
                .delete("/item/10")
                .then()
                .statusCode(200);

        when()
                .get("/item/10")
                .then()
                .statusCode(400);
    }

    @Test
    public void testLikeItem(){
        when()
                .put("/item/like/5fdba127-ab33-4881-bcf8-096e210fe7c9/12")
                .then()
                .statusCode(200);

        when()
                .get("/item/12")
                .then()
                .statusCode(200)
                .body("likesCount", is(2));

        when()
                .put("/item/like/5fdba127-ab33-4881-bcf8-096e210fe7c9/12")
                .then()
                .statusCode(200);

        when()
                .get("/item/12")
                .then()
                .statusCode(200)
                .body("likesCount", is(2));
    }

    @Test
    public void testDeleteLikeFromItem(){
        when()
                .put("/item/like/5fdba127-ab33-4881-bcf8-096e210fe7c9/12")
                .then()
                .statusCode(200);

        when()
                .get("/item/12")
                .then()
                .statusCode(200)
                .body("likesCount", is(2));

        when()
                .delete("/item/like/5fdba127-ab33-4881-bcf8-096e210fe7c9/12")
                .then()
                .statusCode(200);

        when()
                .get("/item/12")
                .then()
                .statusCode(200)
                .body("likesCount", is(1));
    }

    @Test
    public void testGetAllLikesItemsOfUser(){
        when()
                .get("/item/like/fffd85db-55c5-4620-b7eb-73191a43533e")
                .then()
                .statusCode(200)
                .body("size()", is(3))
                .body("find {it.id == 10}", notNullValue())
                .body("find {it.id == 11}", notNullValue())
                .body("find {it.id == 12}", notNullValue());
    }

    @Test
    public void testGetStatisticOfItem(){
        Integer viewingCount = when()
                .get("/item/statistics/10")
                .then()
                .statusCode(200)
                .extract()
                .path("viewingCount");

        Integer addingToCartCount = when()
                .get("/item/statistics/12")
                .then()
                .statusCode(200)
                .extract()
                .path("addingToCartCount");

        when()
                .get("/item/10")
                .then()
                .statusCode(200);

        when()
                .put("/cart/5fdba127-ab33-4881-bcf8-096e210fe7c9/12")
                .then()
                .statusCode(200)
                .body("owner", is("5fdba127-ab33-4881-bcf8-096e210fe7c9"))
                .body("cartItems.size()", is(3))
                .body("cartItems.find { it.id == 12 && it.count == 1 }", notNullValue());

        when()
                .get("/item/statistics/10")
                .then()
                .statusCode(200)
                .body("viewingCount", is(viewingCount+1));

        when()
                .get("/item/statistics/12")
                .then()
                .statusCode(200)
                .body("addingToCartCount", is(addingToCartCount+1));
    }
}
