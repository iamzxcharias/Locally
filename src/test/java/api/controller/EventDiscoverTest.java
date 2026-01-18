package api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class EventDiscoverTest {

    private static final String ALICE_ID = "11111111-1111-1111-1111-111111111111";

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

    @Test
    void discover_shouldReturnUpcomingByDefault() {
        String token = "discover_" + System.nanoTime();
        String category = "DISC_" + token;

        createEvent("PAST " + token, category, "2000-01-01T00:00:00");
        createEvent("FUTURE " + token, category, "2999-01-01T00:00:00");

        given()
                .queryParam("category", category)
                .queryParam("page", 0)
                .queryParam("size", 50)
        .when()
                .get("/events/discover")
        .then()
                .statusCode(200)
                .body("items.title", hasItem("FUTURE " + token))
                .body("items.title", not(hasItem("PAST " + token)));
    }
}