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
public class EventResourceTest {

    static String eventId;
    // Die ID von Alice aus der MockData.sql
    static final String ALICE_ID = "11111111-1111-1111-1111-111111111111";

    @Test
    @Order(1)
    public void testCreateEvent() {
        String jsonBody = String.format("""
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
                """, ALICE_ID);

        eventId = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/events")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", is("Kicker Turnier"))
                .extract()
                .path("id");
    }

    @Test
    @Order(2)
    public void testGetEvent() {
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
    public void testUpdateEvent() {
        String updatedBody = String.format("""
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
                """, ALICE_ID);

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
    public void testDeleteEvent() {
        given()
                .pathParam("id", eventId)
                .when()
                .delete("/events/{id}")
                .then()
                .statusCode(204);

        // Check 404
        given()
                .pathParam("id", eventId)
                .when()
                .get("/events/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(5)
    public void testCreateEventWithEmptyTitle_ShouldReturn400() {
        // Ein JSON-Body mit einem leeren Titel
        String invalidJsonBody = String.format("""
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
            """, ALICE_ID);

        given()
                .contentType(ContentType.JSON)
                .body(invalidJsonBody)
                .when()
                .post("/events")
                .then()
                .statusCode(400); // Wir erwarten hier einen Bad Request (400) wegen @NotBlank
    }
}
