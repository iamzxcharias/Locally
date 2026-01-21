package api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class EventSearchTest {
    @BeforeEach
    public void setup() {
        String jsonBody = """
            {
                "title": "Kicker Turnier SearchTest",
                "description": "Wir testen die Suche",
                "category": "SOCIAL",
                "startsAt": "2026-05-20T18:00:00",
                "placeName": "Büro",
                "lat": 10.0,
                "lng": 10.0,
                "creatorId": "11111111-1111-1111-1111-111111111111"
            }
        """;
        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/events");
    }

    @Test
    public void testSearchByKeyword() {
        given()
                .queryParam("q", "Kicker")
                .when()
                .get("/events")
                .then()
                .statusCode(200)
                .body("items.size()", greaterThan(0)); // Jetzt sollte er es finden!
    }

    @Test
    public void testSearchByCategory() {
        given()
                .queryParam("category", "SOCIAL")
                .when()
                .get("/events")
                .then()
                .statusCode(200)
                .body("items.size()", greaterThan(0));
    }

    @Test
    public void testSearchNoMatch() {
        given()
                .queryParam("q", "EinhornRegenbogenParty")
                .when()
                .get("/events")
                .then()
                .statusCode(200)
                .body("items.size()", is(0)); // Sollte leer sein
    }
}