package io.quarkus.workshop.superheroes.fight;

import au.com.dius.pact.consumer.dsl.LambdaDsl;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(
    providerName = "rest-heroes",
    // Port on which the Mockserver started by Pact will listen.
    //!!! You'll need to update the provider port (for the TEST profile) in application.properties!!!!
    port = "8096"
)
class FightsResourceContractTest {

    @Pact(consumer = "rest-fights")
    public V4Pact randomHeroFoundPact(PactDslWithProvider builder) {
        return builder
            .uponReceiving("A request for a random hero")
            .path("/api/heroes/random")
            .method(HttpMethod.GET)
            .headers(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .willRespondWith()
            .status(Response.Status.OK.getStatusCode())
            .headers(Map.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
            .body(LambdaDsl.newJsonBody(body ->
                    body
                        .stringType("name", "Super Baguette Pact")
                        .integerType("level", 44)
                        .stringType("picture", "super_baguette_pact.png")
                ).build()
            )
            .toPact(V4Pact.class);
    }

    @Pact(consumer = "rest-fights")
    public V4Pact randomHeroNotFoundPact(PactDslWithProvider builder) {
        return builder
            // Requires the use of a state (on the provider side) with this text
            .given("No random hero found")
            .uponReceiving("A request for a random hero")
            .path("/api/heroes/random")
            .method(HttpMethod.GET)
            .headers(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .willRespondWith()
            .status(Response.Status.NOT_FOUND.getStatusCode())
            .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "randomHeroFoundPact")
    void Should_get_random_fighters() {

        // Act + Assert
        var fighters = given()
            .log().all()
            .accept(MediaType.APPLICATION_JSON)
            .when()
            .get("/api/fights/randomfighters")
            .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .as(Fighters.class);

        Assertions.assertEquals(44, fighters.hero().level());
        Assertions.assertEquals("Super Baguette Pact", fighters.hero().name());
        Assertions.assertEquals("super_baguette_pact.png", fighters.hero().picture());
    }

    @Test
    @PactTestFor(pactMethod = "randomHeroNotFoundPact")
    void Should_get_fallback_hero_When_hero_api_return_not_found() {
        Fighters fighters = given()
            .when()
            .get("/api/fights/randomfighters")
            .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .as(Fighters.class);


        Assertions.assertEquals("Fallback hero", fighters.hero().name());
        Assertions.assertEquals("https://dummyimage.com/280x380/1e8fff/ffffff&text=Fallback+Hero", fighters.hero().picture());
        Assertions.assertEquals(1, fighters.hero().level());
    }
}
