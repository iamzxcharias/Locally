package api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Damit wir die CRUD-Reihenfolge einhalten
public class UserResourceTest {

    static String userId; // Wir speichern die ID für nachfolgende Tests

    @Test
    @Order(1)
    public void testCreateUser() {
        String jsonBody = """
                {
                  "name": "Test User",
                  "email": "test@locally.de"
                }
                """;

        userId = given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", is("Test User"))
                .extract()
                .path("id");
    }

    @Test
    @Order(2)
    public void testGetUser() {
        given()
                .pathParam("id", userId)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(200)
                .body("id", is(userId))
                .body("email", is("test@locally.de"));
    }

    @Test
    @Order(3)
    public void testUpdateUser() {
        String updatedJson = """
                {
                  "name": "Updated Name",
                  "email": "test@locally.de"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .body(updatedJson)
                .when()
                .put("/users/{id}")
                .then()
                .statusCode(200)
                .body("name", is("Updated Name"));
    }

    @Test
    @Order(4)
    public void testDeleteUser() {
        given()
                .pathParam("id", userId)
                .when()
                .delete("/users/{id}")
                .then()
                .statusCode(204);

        // Check if really gone
        given()
                .pathParam("id", userId)
                .when()
                .get("/users/{id}")
                .then()
                .statusCode(404); // Oder 500/204, je nachdem wie dein Service Fehler wirft
    }
}
