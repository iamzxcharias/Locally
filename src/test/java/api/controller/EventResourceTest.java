package api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EventResourceTest {

    private static String eventId;
    private static final String ALICE_ID = "11111111-1111-1111-1111-111111111111";

    @Test
    @Order(1)
    void testCreateEvent() {
        String jsonBody = """
                {
                  "title": "Kicker Turnier",
                  "description": "Feierabend-Zocken",
                  "category": "SOCIAL",
                  "startsAt": "2026-05-20T18:00:00",
                  "placeName": "Büro Küche",
                  "lat": 49.0,
                  "lng": 10.0,
                  "creatorId": "%s"
                }
                """.formatted(ALICE_ID);

        eventId = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/events")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", is("Kicker Turnier"))
                .extract().path("id");
    }

    @Test
    @Order(2)
    void testGetEvent() {
        given()
                .pathParam("id", eventId)
                .when()
                .get("/events/{id}")
                .then()
                .statusCode(200)
                .body("title", is("Kicker Turnier"))
                .body("creatorId", is(ALICE_ID));
    }

    @Test
    @Order(3)
    void testUpdateEvent() {
        String updatedBody = """
                {
                  "title": "Großes Kicker Turnier",
                  "description": "Jetzt mit Pizza",
                  "category": "SOCIAL",
                  "startsAt": "2026-05-20T18:00:00",
                  "placeName": "Büro Küche",
                  "lat": 49.0,
                  "lng": 10.0,
                  "creatorId": "%s"
                }
                """.formatted(ALICE_ID);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", eventId)
                .body(updatedBody)
                .when()
                .put("/events/{id}")
                .then()
                .statusCode(200)
                .body("title", is("Großes Kicker Turnier"));
    }

    @Test
    @Order(4)
    void testDeleteEvent() {
        given()
                .pathParam("id", eventId)
                .when()
                .delete("/events/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", eventId)
                .when()
                .get("/events/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(5)
    void testCreateEventWithEmptyTitle_ShouldReturn400() {
        String invalidJsonBody = """
            {
              "title": "", 
              "description": "Ein Test ohne Titel",
              "category": "SOCIAL",
              "startsAt": "2026-05-20T18:00:00",
              "placeName": "Büro",
              "lat": 49.0,
              "lng": 10.0,
              "creatorId": "%s"
            }
            """.formatted(ALICE_ID);

        given()
                .contentType(ContentType.JSON)
                .body(invalidJsonBody)
                .when()
                .post("/events")
                .then()
                .statusCode(400);
    }
}