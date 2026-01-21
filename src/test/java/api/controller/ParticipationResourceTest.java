package api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParticipationResourceTest {

    static String dynamicEventId;
    static String participationId;

    static final String ALICE_ID = "11111111-1111-1111-1111-111111111111";

    @Test
    @Order(1)
    public void testCreateEventForParticipation() {
        String eventJson = String.format("""
                {
                  "title": "Participation Test Event",
                  "description": "Test",
                  "category": "SOCIAL",
                  "startsAt": "2026-10-10T10:00:00",
                  "placeName": "Lab",
                  "lat": 0.0,
                  "lng": 0.0,
                  "creatorId": "%s"
                }
                """, ALICE_ID);

        dynamicEventId = given()
                .contentType(ContentType.JSON)
                .body(eventJson)
                .when()
                .post("/events")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    @Test
    @Order(2)
    public void testRegisterForEvent() {
        String participationJson = String.format("""
                {
                  "userId": "%s",
                  "eventId": "%s",
                  "status": "INTERESTED"
                }
                """, ALICE_ID, dynamicEventId);

        participationId = given()
                .contentType(ContentType.JSON)
                .body(participationJson)
                .when()
                .post("/participations")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Test
    @Order(3)
    public void testGetParticipation() {
        given()
                .pathParam("id", participationId)
                .when()
                .get("/participations/{id}")
                .then()
                .statusCode(200)
                .body("userId", is(ALICE_ID));
    }

    @Test
    @Order(4)
    public void testUpdateParticipationStatus() {
        // WICHTIG: Wir nutzen "GOING", da wir wissen, dass es dieses Enum-Value gibt.
        // Falls dein Enum anders aussieht, nimm einen Wert, der dort definiert ist.
        String patchBody = String.format("""
                {
                  "userId": "%s",
                  "eventId": "%s",
                  "status": "GOING"
                }
                """, ALICE_ID, dynamicEventId);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", participationId)
                .body(patchBody)
                .when()
                .patch("/participations/{id}")
                .then()
                .statusCode(200)
                .body("status", is("GOING"));
    }

    @Test
    @Order(5)
    public void testCancelParticipation() {
        given()
                .pathParam("id", participationId)
                .when()
                .delete("/participations/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", participationId)
                .when()
                .get("/participations/{id}")
                .then()
                .statusCode(404);
    }
}