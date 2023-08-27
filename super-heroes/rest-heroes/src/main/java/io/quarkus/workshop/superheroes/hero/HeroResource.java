package io.quarkus.workshop.superheroes.hero;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@Path("/api/heroes")
@Tag(name = "heroes")
public class HeroResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeroResource.class);

    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    @Tag(name = "hello")
    public String hello() {
        return "Hello Hero Resource";
    }

    @GET
    @Path("random")
    @Operation(summary = "Return random hero")
    @APIResponse(
        responseCode = "200",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(
                implementation = Hero.class,
                required = true
            )
        )
    )
    public Uni<Response> getRandomHero() {
        return Hero.findRandom()
            .onItem().ifNotNull().transform(hero -> Response.ok(hero).build())
            .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build())
            .invoke(response -> LOGGER.debug("Random hero {}", response.getEntity()));
    }

    @GET
    @Operation(summary = "All heroes")
    @APIResponse(
        responseCode = "200",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(
                implementation = Hero.class,
                type = SchemaType.ARRAY
            )
        )
    )
    public Uni<Response> getAllHeroes() {
        return Hero.<Hero>listAll()
            .map(heroes -> Response.ok(heroes).build())
            .invoke(response -> LOGGER.debug("heroes {}", response.getEntity()));
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Hero for given ID")
    @APIResponse(
        responseCode = "200",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(
                implementation = Hero.class
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Hero not found"
    )
    public Uni<Response> getHero(@PathParam("id") Long id) {
        return Hero.<Hero>findById(id)
            .map(hero -> Objects.nonNull(hero) ? Response.ok(hero).build() : Response.status(Response.Status.NOT_FOUND).build())
            .invoke(response -> LOGGER.debug("Response {} {}", response.getStatus(), response.getEntity()));
    }

    @POST
    @Operation(summary = "Create hero")
    @APIResponse(
        responseCode = "201",
        description = "URI of created hero"
    )
    @WithTransaction
    public Uni<Response> createHero(@Valid Hero hero, @Context UriInfo uriInfo) {
        return hero.<Hero>persist()
            .map(heroCreated -> {
                var uri = uriInfo.getAbsolutePathBuilder()
                    .path(Long.toString(heroCreated.id))
                    .build();
                return Response.created(uri).build();
            })
            .invoke(response -> LOGGER.debug("Hero created {}", response.getHeaderString(HttpHeaders.LOCATION)));
    }

    @PUT
    @Operation(summary = "Update hero")
    @APIResponse(
        responseCode = "200",
        description = "Updated heroe",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(
                implementation = Hero.class
            )
        )
    )
    @WithTransaction
    public Uni<Response> updateHero(@Valid Hero hero) {
        return Hero.<Hero>findById(hero.id)
            .invoke(heroToUpdate -> {
                if (Objects.nonNull(heroToUpdate)) heroToUpdate.merge(hero);
            })
            .map(heroUpdated -> Objects.nonNull(heroUpdated) ? Response.ok(heroUpdated).build() : Response.status(Response.Status.BAD_REQUEST).build())
            .invoke(response -> LOGGER.debug("Hero updated {} {}", response.getStatus(), response.getEntity()));
    }

    @DELETE
    @Path("{id}")
    @Operation(summary = "Delete hero for given ID")
    @APIResponse(responseCode = "204")
    @WithTransaction
    public Uni<Response> deleteHero(@PathParam("id") Long id) {
        return Hero.<Hero>deleteById(id)
            .invoke(hero -> LOGGER.debug("Deleted hero {}", hero))
            .replaceWith(Response.noContent().build());
    }
}
