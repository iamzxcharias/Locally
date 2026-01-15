package api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class EventQueryTest {

    static final String ALICE_ID = "11111111-1111-1111-1111-111111111111";

    private String createEvent(String title, String category, String description, String startsAt) {
        String body = String.format("""
                {
                  "title": "%s",
                  "description": %s,
                  "category": "%s",
                  "startsAt": "%s",
                  "placeName": "Lab",
                  "lat": 49.0,
                  "lng": 10.0,
                  "creatorId": "%s"
                }
                """,
                title,
                (description == null ? "null" : "\"" + description + "\""),
                category,
                startsAt,
                ALICE_ID
        );

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
    void filterByCategory_shouldReturnOnlyThatCategory() {
        createEvent("Filter A1", "FILTER_CAT", null, "2026-01-10T10:00:00");
        createEvent("Filter A2", "FILTER_CAT", "desc", "2026-01-11T10:00:00");
        createEvent("Filter B1", "OTHER_CAT", "desc", "2026-01-12T10:00:00");

        given()
                .queryParam("category", "FILTER_CAT")
                .queryParam("page", 0)
                .queryParam("size", 20)
        .when()
                .get("/events")
        .then()
                .statusCode(200)
                .body("items.size()", greaterThanOrEqualTo(2))
                .body("items.category", everyItem(equalTo("FILTER_CAT")));
    }

    @Test
    void paging_shouldReturnDifferentItemsOnDifferentPages() {
        createEvent("Page 1", "PAGING_CAT", "desc", "2026-02-10T10:00:00");
        createEvent("Page 2", "PAGING_CAT", "desc", "2026-02-11T10:00:00");

        String idPage0 =
                given()
                        .queryParam("category", "PAGING_CAT")
                        .queryParam("page", 0)
                        .queryParam("size", 1)
                .when()
                        .get("/events")
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
                        .queryParam("category", "PAGING_CAT")
                        .queryParam("page", 1)
                        .queryParam("size", 1)
                .when()
                        .get("/events")
                .then()
                        .statusCode(200)
                        .body("items.size()", is(1))
                        .body("page", is(1))
                        .body("size", is(1))
                        .body("total", greaterThanOrEqualTo(2))
                        .extract()
                        .path("items[0].id");

        org.junit.jupiter.api.Assertions.assertNotEquals(idPage0, idPage1);
    }

    @Test
    void cacheControlHeader_shouldBeSet() {
        given()
                .queryParam("page", 0)
                .queryParam("size", 20)
        .when()
                .get("/events")
        .then()
                .statusCode(200)
                .header("Cache-Control", Matchers.containsString("max-age=30"));
    }

    @Test
    void qSearch_shouldNotCrashWhenDescriptionIsNull() {
        createEvent("SearchTitle", "SEARCH_CAT", null, "2026-03-10T10:00:00");

        given()
                .queryParam("q", "search")
                .queryParam("page", 0)
                .queryParam("size", 20)
        .when()
                .get("/events")
        .then()
                .statusCode(200);
    }
}