package io.quarkus.workshop.superheroes.fight;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@Path("/api/fights")
@Tag(name = "fights")
public class FightResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(FightResource.class);

    private final FightService fightService;

    public FightResource(FightService fightService) {
        this.fightService = fightService;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("hello")
    @Tag(name = "hello")
    public String hello() {
        return "Hello Fight Resource";
    }

    @GET
    @Path("randomfighters")
    @Operation(summary = "Random fighters")
    @APIResponse(
        responseCode = "200",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(
                required = true,
                implementation = Fighters.class
            )
        )
    )
    public Response getRandomFighters() {

        var fighters = this.fightService.findRandomFighters();

        LOGGER.debug("Random fighters {}", fighters);

        return Response.ok(fighters).build();
    }

    @GET
    @Operation(summary = "All fights")
    @APIResponse(
        responseCode = "200",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(
                implementation = Fight.class,
                type = SchemaType.ARRAY
            )
        )
    )
    public Response getAllFights() {

        var fights = this.fightService.findAllFights();

        LOGGER.debug("Total size {}", fights.size());

        return Response.ok(fights).build();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Fight for given ID")
    @APIResponse(
        responseCode = "200",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(
                implementation = Fight.class
            )
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Fight not found"
    )
    public Response getFight(@PathParam("id") Long id) {

        var fight = this.fightService.findFightById(id);

        if (Objects.nonNull(fight)) {
            LOGGER.debug("Fight {}", fight);
            return Response.ok(fight).build();
        }

        LOGGER.debug("Fight not found");
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Operation(summary = "Start fight")
    @APIResponse(
        responseCode = "201",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(
                implementation = Fight.class
            )
        )
    )
    public Response fight(@Valid Fighters fighters, @Context UriInfo uriInfo) {

        var fight = this.fightService.createFight(fighters);

        var uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(fight.id)).build();

        LOGGER.debug("Fight created {}", uri);

        return Response.created(uri).build();
    }
}
