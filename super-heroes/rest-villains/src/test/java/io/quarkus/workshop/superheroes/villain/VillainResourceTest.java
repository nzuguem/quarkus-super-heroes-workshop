package io.quarkus.workshop.superheroes.villain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VillainResourceTest {

    private static String villainId;

    @Test
    void Should_say_hello() {

        // Act + Assert
        given()
            .log().all()
          .when().get("/api/villains/hello")
          .then()
             .statusCode(200)
             .body(is("Hello Villain Resource"));
    }

    @Test
    void Should_not_found_When_get_unknown_villain() {
        // Arrange
        var unknownVillainId = 123456789951L;

        // Act + Assert
        given()
            .log().all()
            .pathParam("id", unknownVillainId)
            .when()
            .get("/api/villains/{id}")
            .then()
            .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void Should_get_random_villain() {

        // Act + Assert
        given()
            .log().all()
            .when()
            .get("/api/villains/random")
            .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void Should_bad_request_When_name_level_villain_are_invalid() throws IOException {

        // Arrange
        var villainJson = """
            {
                "name": null,
                "otherName": "Super Chocolatine chocolate in",
                "picture": "super_chocolatine.png",
                "powers": "does not eat pain au chocolat",
                "level": 0
            }
            """;

        var villain = TestUtils.jsonToVillain(villainJson);

        // Act + Assert
        given()
            .log().all()
            .body(villain)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .post("/api/villains")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(1)
    void Should_get_all_villains() {

        // Act + Assert
        given()
            .log().all()
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .get("/api/villains")
            .then()
            .body("size()", is(3));
    }

    @Test
    @Order(2)
    void Should_add_villain() throws JsonProcessingException {

        // Arrange
        var villainJson = """
            {
                "name": "Super Chocolatine",
                "otherName": "Super Chocolatine chocolate in",
                "picture": "super_chocolatine.png",
                "powers": "does not eat pain au chocolat",
                "level": 42
            }
            """;

        var villain = TestUtils.jsonToVillain(villainJson);

        // Act + Assert
        var location = given()
            .log().all()
            .body(villain)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .post("/api/villains")
            .then()
            .statusCode(Response.Status.CREATED.getStatusCode())
            .extract().header(HttpHeaders.LOCATION);

        var locationSegments = location.split("/");
        villainId = locationSegments[locationSegments.length - 1];
    }

    @Test
    @Order(3)
    void Should_update_villain() throws JsonProcessingException {

        // Arrange
        var villainJson = String.format("""
            {
                "id" : %d,
                "name": "Super Chocolatine (updated)",
                "otherName": "Super Chocolatine chocolate in (updated)",
                "picture": "super_chocolatine_updated.png",
                "powers": "does not eat pain au chocolat (updated)",
                "level": 42
            }
            """, Long.valueOf(villainId));

        var villain = TestUtils.jsonToVillain(villainJson);

        // Act + Assert
        given()
            .log().all()
            .body(villain)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .put("/api/villains")
            .then()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(4)
    void Should_remove_villain() {

        // Act + Assert
        given()
            .pathParam("id", villainId)
            .when()
            .delete("/api/villains/{id}")
            .then()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    void Should_get_open_api_spec() {

        // Act + Assert
        given()
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .get("/q/openapi")
            .then()
            .statusCode(Response.Status.OK.getStatusCode());
    }

}
