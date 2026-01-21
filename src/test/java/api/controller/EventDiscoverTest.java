package api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class EventDiscoverTest {

    private static final String ALICE_ID = "11111111-1111-1111-1111-111111111111";

    @Test
    void discover_shouldReturnUpcomingByDefault() {
        String token = "discover_" + System.nanoTime();
        String category = "DISC_" + token;
        String eventTitle = "FUTURE " + token;

        createEvent(eventTitle, category, "2999-01-01T00:00:00");

        given()
                .queryParam("category", category)
                .queryParam("page", 0)
                .queryParam("size", 50)
                .when()
                .get("/events/discover")
                .then()
                .statusCode(200)
                .body("items.title", hasItem(eventTitle));
    }

    private String createEvent(String title, String category, String startsAt) {
        String jsonBody = """
                {
                  "title": "%s",
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
                .body(jsonBody)
                .when()
                .post("/events")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }
}