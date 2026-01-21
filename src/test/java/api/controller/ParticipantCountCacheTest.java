package api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ParticipantCountCacheTest {

    private static final String ALICE_ID = "11111111-1111-1111-1111-111111111111";

    @Test
    void participantCount_shouldUpdateOnCreateAndDelete() {
        String token = "pc_" + System.nanoTime();

        String userId = createUser("PCUser " + token, "pc_" + token + "@locally.de");
        String eventId = createEvent("PCEvent " + token, "PCAT_" + token, "2999-01-01T10:00:00");

        given()
                .pathParam("id", eventId)
                .when()
                .get("/events/{id}")
                .then()
                .statusCode(200)
                .body("participantCount", is(0));

        String participationId = createParticipation(userId, eventId, "GOING");

        given()
                .pathParam("id", eventId)
                .when()
                .get("/events/{id}")
                .then()
                .statusCode(200)
                .body("participantCount", is(1));

        given()
                .pathParam("id", participationId)
                .when()
                .delete("/participations/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", eventId)
                .when()
                .get("/events/{id}")
                .then()
                .statusCode(200)
                .body("participantCount", is(0));
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

    private String createEvent(String title, String category, String startsAt) {
        String body = """
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
                """.formatted(title, category, startsAt, ALICE_ID);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/events")
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    private String createParticipation(String userId, String eventId, String status) {
        String body = """
                {
                  "userId": "%s",
                  "eventId": "%s",
                  "status": "%s"
                }
                """.formatted(userId, eventId, status);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/participations")
                .then()
                .statusCode(201)
                .extract().path("id");
    }
}