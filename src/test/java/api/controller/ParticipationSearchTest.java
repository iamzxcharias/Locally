package api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ParticipationSearchTest {

    private static final String ALICE_ID = "11111111-1111-1111-1111-111111111111";

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

    private String createEvent(String title, String category, String startsAt) {
        String body = String.format("""
                {
                  "title": "%s",
                  "description": null,
                  "category": "%s",
                  "startsAt": "%s",
                  "placeName": "Lab",
                  "lat": 49.0,
                  "lng": 10.0,
                  "creatorId": "%s"
                }
                """, title, category, startsAt, ALICE_ID);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/events")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    private String createParticipation(String userId, String eventId, String status) {
        String body = String.format("""
                {
                  "userId": "%s",
                  "eventId": "%s",
                  "status": "%s"
                }
                """, userId, eventId, status);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/participations")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    @Test
    void participationsSearch_shouldFilterByUserIdEventIdStatus_andSupportPaging_andCacheHeader() {
        String token = "p_" + System.nanoTime();

        String u1 = createUser("PUser1 " + token, "p1_" + token + "@locally.de");
        String u2 = createUser("PUser2 " + token, "p2_" + token + "@locally.de");

        String eventId = createEvent("PEvent " + token, "PCAT_" + token, "2999-01-01T10:00:00");

        String p1 = createParticipation(u1, eventId, "GOING");
        createParticipation(u2, eventId, "INTERESTED");

        // filter: eventId + status=GOING -> nur p1
        given()
                .queryParam("eventId", eventId)
                .queryParam("status", "GOING")
                .queryParam("page", 0)
                .queryParam("size", 50)
        .when()
                .get("/participations/search")
        .then()
                .statusCode(200)
                .body("items.id", hasItem(p1))
                .body("items.size()", is(1))
                .body("items[0].eventId", equalTo(eventId))
                .body("items[0].status", equalTo("GOING"));

        // paging: mindestens 2 GOING
        String u3 = createUser("PUser3 " + token, "p3_" + token + "@locally.de");
        createParticipation(u3, eventId, "GOING");

        String idPage0 =
                given()
                        .queryParam("eventId", eventId)
                        .queryParam("status", "GOING")
                        .queryParam("page", 0)
                        .queryParam("size", 1)
                .when()
                        .get("/participations/search")
                .then()
                        .statusCode(200)
                        .body("items.size()", is(1))
                        .body("page", is(0))
                        .body("size", is(1))
                        .body("total", greaterThanOrEqualTo(2))
                        .extract()
                        .path("items[0].id");

        String idPage1 =
                given()
                        .queryParam("eventId", eventId)
                        .queryParam("status", "GOING")
                        .queryParam("page", 1)
                        .queryParam("size", 1)
                .when()
                        .get("/participations/search")
                .then()
                        .statusCode(200)
                        .body("items.size()", is(1))
                        .body("page", is(1))
                        .body("size", is(1))
                        .body("total", greaterThanOrEqualTo(2))
                        .extract()
                        .path("items[0].id");

        org.junit.jupiter.api.Assertions.assertNotEquals(idPage0, idPage1);

        // cache header
        given()
                .queryParam("page", 0)
                .queryParam("size", 20)
        .when()
                .get("/participations/search")
        .then()
                .statusCode(200)
                .header("Cache-Control", Matchers.containsString("max-age=30"));
    }
}