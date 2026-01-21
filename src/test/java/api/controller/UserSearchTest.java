package api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@QuarkusTest
class UserSearchTest {

    @Test
    void usersSearch_shouldFilterByNameOrEmail_andReturnEnvelope() {
        String token = "u_search_" + System.nanoTime();

        String id1 = createUser("Name " + token, token + "_1@" + token + ".de");
        String id2 = createUser("User " + token, token + "_2@" + token + ".de");

        given()
                .queryParam("q", token)
                .queryParam("page", 0)
                .queryParam("size", 50)
                .when()
                .get("/users/search")
                .then()
                .statusCode(200)
                .body("items.id", hasItems(id1, id2))
                .body("page", is(0))
                .body("size", is(50))
                .body("total", greaterThanOrEqualTo(2));
    }

    @Test
    void usersSearch_shouldPageResults() {
        String token = "u_page_" + System.nanoTime();

        createUser("A " + token, "a_" + token + "@locally.de");
        createUser("B " + token, "b_" + token + "@locally.de");

        String idPage0 = given()
                .queryParam("q", token)
                .queryParam("page", 0)
                .queryParam("size", 1)
                .when()
                .get("/users/search")
                .then()
                .statusCode(200)
                .body("items.size()", is(1))
                .body("page", is(0))
                .body("size", is(1))
                .body("total", greaterThanOrEqualTo(2))
                .extract().path("items[0].id");

        String idPage1 = given()
                .queryParam("q", token)
                .queryParam("page", 1)
                .queryParam("size", 1)
                .when()
                .get("/users/search")
                .then()
                .statusCode(200)
                .body("items.size()", is(1))
                .body("page", is(1))
                .body("size", is(1))
                .body("total", greaterThanOrEqualTo(2))
                .extract().path("items[0].id");

        assertNotEquals(idPage0, idPage1);
    }

    @Test
    void usersSearch_shouldReturnCacheControlHeader() {
        given()
                .queryParam("q", "whatever")
                .queryParam("page", 0)
                .queryParam("size", 20)
                .when()
                .get("/users/search")
                .then()
                .statusCode(200)
                .header("Cache-Control", containsString("max-age=30"));
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
}