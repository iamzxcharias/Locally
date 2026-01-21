package api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@QuarkusTest
class FriendshipSearchTest {

    @Test
    void friendshipsSearch_shouldFilterByStatus_friendId_friendQ_andSupportPaging_andCacheHeader() {
        String token = "fq_" + System.nanoTime();

        String me = createUser("Me " + token, "me_" + token + "@locally.de");
        String friendB = createUser("BestFriend " + token, "best_" + token + "@locally.de");
        String friendC = createUser("OtherFriend " + token, "other_" + token + "@locally.de");

        String fAccepted = requestFriendship(me, friendB);
        acceptFriendship(fAccepted);

        String fPending = requestFriendship(me, friendC);

        given()
                .pathParam("userId", me)
                .queryParam("status", "ACCEPTED")
                .queryParam("page", 0)
                .queryParam("size", 50)
                .when()
                .get("/friendships/user/{userId}/search")
                .then()
                .statusCode(200)
                .body("items.id", hasItem(fAccepted))
                .body("items.id", not(hasItem(fPending)));

        given()
                .pathParam("userId", me)
                .queryParam("friendId", friendB)
                .queryParam("page", 0)
                .queryParam("size", 50)
                .when()
                .get("/friendships/user/{userId}/search")
                .then()
                .statusCode(200)
                .body("items.id", hasItem(fAccepted))
                .body("items.id", not(hasItem(fPending)));

        given()
                .pathParam("userId", me)
                .queryParam("friendQ", "best_" + token)
                .queryParam("page", 0)
                .queryParam("size", 50)
                .when()
                .get("/friendships/user/{userId}/search")
                .then()
                .statusCode(200)
                .body("items.id", hasItem(fAccepted))
                .body("items.id", not(hasItem(fPending)));

        String friendD = createUser("D " + token, "d_" + token + "@locally.de");
        String friendE = createUser("E " + token, "e_" + token + "@locally.de");

        acceptFriendship(requestFriendship(me, friendD));
        acceptFriendship(requestFriendship(me, friendE));

        String idPage0 = given()
                .pathParam("userId", me)
                .queryParam("status", "ACCEPTED")
                .queryParam("page", 0)
                .queryParam("size", 1)
                .when()
                .get("/friendships/user/{userId}/search")
                .then()
                .statusCode(200)
                .body("items.size()", is(1))
                .body("page", is(0))
                .body("total", greaterThanOrEqualTo(3))
                .extract().path("items[0].id");

        String idPage1 = given()
                .pathParam("userId", me)
                .queryParam("status", "ACCEPTED")
                .queryParam("page", 1)
                .queryParam("size", 1)
                .when()
                .get("/friendships/user/{userId}/search")
                .then()
                .statusCode(200)
                .body("items.size()", is(1))
                .body("page", is(1))
                .extract().path("items[0].id");

        assertNotEquals(idPage0, idPage1);

        given()
                .pathParam("userId", me)
                .queryParam("page", 0)
                .queryParam("size", 20)
                .when()
                .get("/friendships/user/{userId}/search")
                .then()
                .statusCode(200)
                .header("Cache-Control", containsString("max-age=30"));
    }

    private String createUser(String name, String email) {
        String body = """
                {
                  "name": "%s",
                  "email": "%s"
                }
                """.formatted(name, email);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    private String requestFriendship(String requesterId, String addresseeId) {
        String body = """
                {
                  "requesterId": "%s",
                  "addresseeId": "%s"
                }
                """.formatted(requesterId, addresseeId);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/friendships")
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    private void acceptFriendship(String friendshipId) {
        given()
                .when()
                .patch("/friendships/{id}/accept", friendshipId)
                .then()
                .statusCode(200)
                .body("status", is("ACCEPTED"));
    }
}