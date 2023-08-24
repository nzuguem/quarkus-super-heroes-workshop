package io.quarkus.workshop.superheroes.hero;

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
class HeroResourceTest {

    private static String heroId;

    @Test
    void Should_say_hello() {

        // Act + Assert
        given()
          .when().get("/api/heroes/hello")
          .then()
             .statusCode(200)
             .body(is("Hello Hero Resource"));
    }

    @Test
    void Should_not_found_When_get_unknown_hero() {
        // Arrange
        var unknownHeroId = 123456789951L;

        // Act + Assert
        given()
            .log().all()
            .pathParam("id", unknownHeroId)
            .when()
            .get("/api/heroes/{id}")
            .then()
            .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void Should_get_random_hero() {

        // Act + Assert
        given()
            .log().all()
            .when()
            .get("/api/heroes/random")
            .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void Should_bad_request_When_name_level_hero_are_invalid() throws IOException {

        // Arrange
        var heroJson = """
            {
                "name": null,
                "otherName": "Super Baguette Tradition",
                "picture": "super_baguette.png",
                "powers": "eats baguette really quickly",
                "level": 0
            }
            """;

        var hero = TestUtils.jsonToHero(heroJson);

        // Act + Assert
        given()
            .log().all()
            .body(hero)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .post("/api/heroes")
            .then()
            .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(1)
    void Should_get_all_heroes() {

        // Act + Assert
        given()
            .log().all()
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .get("/api/heroes")
            .then()
            .body("size()", is(941));
    }

    @Test
    @Order(2)
    void Should_add_hero() throws JsonProcessingException {

        // Arrange
        var heroJson ="""
            {
                "name": "Super Baguette",
                "otherName": "Super Baguette Tradition",
                "picture": "super_baguette.png",
                "powers": "eats baguette really quickly",
                "level": 42
            }
            """;

        var hero = TestUtils.jsonToHero(heroJson);

        // Act + Assert
        var location = given()
            .log().all()
            .body(hero)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .post("/api/heroes")
            .then()
            .statusCode(Response.Status.CREATED.getStatusCode())
            .extract().header(HttpHeaders.LOCATION);

        var locationSegments = location.split("/");
        heroId = locationSegments[locationSegments.length - 1];
    }

    @Test
    @Order(3)
    void Should_update_hero() throws JsonProcessingException {

        // Arrange
        var helloJson = String.format("""
            {
                "id" : %d,
                "name": "Super Baguette (updated)",
                "otherName": "Super Baguette Tradition (updated)",
                "picture": "super_baguette_updated.png",
                "powers": "eats baguette really quickly (updated)",
                "level": 43
            }
            """, Long.valueOf(heroId));

        var hero = TestUtils.jsonToHero(helloJson);

        // Act + Assert
        given()
            .log().all()
            .body(hero)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .put("/api/heroes")
            .then()
            .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(4)
    void Should_remove_hero() {

        // Act + Assert
        given()
            .pathParam("id", heroId)
            .when()
            .delete("/api/heroes/{id}")
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
