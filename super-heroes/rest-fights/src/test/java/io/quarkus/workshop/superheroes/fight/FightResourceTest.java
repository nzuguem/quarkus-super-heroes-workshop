package io.quarkus.workshop.superheroes.fight;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FightResourceTest {

    private static String fightId;

    @Test
    void Should_say_hello() {
        given()
            .when().get("/api/fights/hello")
            .then()
            .statusCode(200)
            .body(is("Hello Fight Resource"));
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

    @Test
    void Should_not_found_When_get_unknown_fight() {
        // Arrange
        var unknownFightId = 123456789951L;

        // Act + Assert
        given()
            .log().all()
            .pathParam("id", unknownFightId)
            .when()
            .get("/api/fights/{id}")
            .then()
            .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void Should_bad_request_When_hero_villain_fight_invalid() throws IOException {

        // Arrange
        var fightersJson = """
            {
                "hero": null,
                "villain": null
            }
            """;

        var fighters = TestUtils.jsonToFighters(fightersJson);

        // Act + Assert
        given()
            .log().all()
            .body(fighters)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .post("/api/fights")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(1)
    void Should_get_all_fights() {

        // Act + Assert
        given()
            .log().all()
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .get("/api/fights")
            .then()
            .body("size()", is(3));
    }

    @Test
    @Order(2)
    void Should_fight() throws JsonProcessingException {

        // Arrange
        var fightersJson = """
            {
               "hero": {
                  "name": "Super Baguette",
                  "picture": "super_baguette.png",
                  "level": 42
               },
               "villain": {
                  "name": "Super Chocolatine",
                  "picture": "super_chocolatine.png",
                  "level": 6
               }
            }
            """;

        var fighters = TestUtils.jsonToFighters(fightersJson);

        // Act + Assert
        var location = given()
            .log().all()
            .body(fighters)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .post("/api/fights")
            .then()
            .statusCode(Response.Status.CREATED.getStatusCode())
            .extract().header(HttpHeaders.LOCATION);

        var locationSegments = location.split("/");
        fightId = locationSegments[locationSegments.length - 1];
    }

}
