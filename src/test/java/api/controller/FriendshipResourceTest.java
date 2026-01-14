package api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FriendshipResourceTest {

    static String friendshipId;

    // IDs aus der MockData.sql
    static final String ALICE_ID = "11111111-1111-1111-1111-111111111111";
    static final String BOB_ID = "22222222-2222-2222-2222-222222222222";

    @Test
    @Order(1)
    public void testRequestFriendship() {
        // Alice fragt Bob an
        String jsonBody = String.format("""
                {
                  "requesterId": "%s",
                  "addresseeId": "%s"
                }
                """, ALICE_ID, BOB_ID);

        friendshipId = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/friendships")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("status", is("PENDING")) // Standardmäßig sollte es PENDING sein
                .extract()
                .path("id");
    }

    @Test
    @Order(2)
    public void testGetFriendshipsForUser() {
        // Prüfen, ob Alice die Anfrage in ihrer Liste hat
        given()
                .pathParam("userId", ALICE_ID)
                .when()
                .get("/friendships/user/{userId}")
                .then()
                .statusCode(200)
                .body("status", hasItems("PENDING"));
    }

    @Test
    @Order(3)
    public void testAcceptFriendship() {
        // Bob akzeptiert die Anfrage
        given()
                .when()
                .patch("/friendships/" + friendshipId + "/accept")
                .then()
                .statusCode(200)
                .body("status", is("ACCEPTED"));
    }

    @Test
    @Order(4)
    public void testDeleteFriendship() {
        // Freundschaft kündigen / Anfrage zurückziehen
        given()
                .when()
                .delete("/friendships/" + friendshipId)
                .then()
                .statusCode(204);
    }
}
