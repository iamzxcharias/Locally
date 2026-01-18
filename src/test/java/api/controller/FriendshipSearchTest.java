package api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class FriendshipSearchTest {

    private String createUser(String name, String email) {
        String body = String.format("""
                {
                  "name": "%s",
                  "email": "%s"
                }
                """, name, email);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    private String requestFriendship(String requesterId, String addresseeId) {
        String body = String.format("""
                {
                  "requesterId": "%s",
                  "addresseeId": "%s"
                }
                """, requesterId, addresseeId);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/friendships")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    private void acceptFriendship(String friendshipId) {
        given()
                .when()
                .patch("/friendships/" + friendshipId + "/accept")
                .then()
                .statusCode(200)
                .body("status", is("ACCEPTED"));
    }

    @Test
    void friendshipsSearch_shouldFilterByStatus_friendId_friendQ_andSupportPaging_andCacheHeader() {
        String token = "fq_" + System.nanoTime();

        String me = createUser("Me " + token, "me_" + token + "@locally.de");

        String friendBName = "BestFriend " + token;
        String friendBEmail = "best_" + token + "@locally.de";
        String friendB = createUser(friendBName, friendBEmail);

        String friendC = createUser("OtherFriend " + token, "other_" + token + "@locally.de");

        String fAccepted = requestFriendship(me, friendB);
        acceptFriendship(fAccepted);

        String fPending = requestFriendship(me, friendC);

        // status=ACCEPTED -> nur fAccepted
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

        // friendId -> genau fAccepted
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

        // friendQ (name/email) -> matcht friendB
        given()
                .pathParam("userId", me)
                .queryParam("friendQ", token)
                .queryParam("page", 0)
                .queryParam("size", 50)
        .when()
                .get("/friendships/user/{userId}/search")
        .then()
                .statusCode(200)
                .body("items.id", hasItem(fAccepted))
                .body("items.id", not(hasItem(fPending)));

        // paging (mit status=ACCEPTED und size=1 müssen page0/page1 verschieden sein, wenn >=2)
        String friendD = createUser("D " + token, "d_" + token + "@locally.de");
        String friendE = createUser("E " + token, "e_" + token + "@locally.de");

        String f2 = requestFriendship(me, friendD);
        acceptFriendship(f2);

        String f3 = requestFriendship(me, friendE);
        acceptFriendship(f3);

        String idPage0 =
                given()
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
                        .body("size", is(1))
                        .body("total", greaterThanOrEqualTo(3))
                        .extract()
                        .path("items[0].id");

        String idPage1 =
                given()
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
                        .body("size", is(1))
                        .body("total", greaterThanOrEqualTo(3))
                        .extract()
                        .path("items[0].id");

        org.junit.jupiter.api.Assertions.assertNotEquals(idPage0, idPage1);

        // cache header
        given()
                .pathParam("userId", me)
                .queryParam("page", 0)
                .queryParam("size", 20)
        .when()
                .get("/friendships/user/{userId}/search")
        .then()
                .statusCode(200)
                .header("Cache-Control", Matchers.containsString("max-age=30"));
    }
}
