package api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FriendshipResourceTest {

    static String friendshipId;

    static final String DIANA_ID = "44444444-4444-4444-4444-444444444444";
    static final String BOB_ID = "22222222-2222-2222-2222-222222222222";

    @Test
    @Order(1)
    public void testRequestFriendship() {
        String jsonBody = String.format("""
                {
                  "requesterId": "%s",
                  "addresseeId": "%s"
                }
                """, DIANA_ID, BOB_ID);

        friendshipId = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/friendships")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Test
    @Order(2)
    public void testAcceptFriendship() {
        // Jetzt funktioniert der 200er, weil friendshipId nicht mehr null ist
        given()
                .when()
                .patch("/friendships/" + friendshipId + "/accept")
                .then()
                .statusCode(200)
                .body("status", is("ACCEPTED"));
    }

    @Test
    @Order(3)
    public void testDeleteFriendship() {
        given()
                .when()
                .delete("/friendships/" + friendshipId)
                .then()
                .statusCode(204);
    }
}
