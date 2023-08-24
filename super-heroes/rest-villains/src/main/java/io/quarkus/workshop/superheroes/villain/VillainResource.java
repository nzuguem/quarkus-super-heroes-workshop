package io.quarkus.workshop.superheroes.villain;

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

import java.net.URI;
import java.util.Objects;

@Path("/api/villains")
@Tag(ref = "villains")
public class VillainResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(VillainResource.class);

    private final VillainService villainService;

    public VillainResource(VillainService villainService) {
        this.villainService = villainService;
    }

    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    @Tag(name = "hello")
    public String hello() {
        return "Hello Villain Resource";
    }

    @GET
    @Path("random")
    @Operation(summary = "Return random villain")
    @APIResponse(
        responseCode = "200",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(
                implementation = Villain.class,
                required = true)
        )
    )
    public Response getRandomVillain() {

        var randomVillain = this.villainService.findRandomVillain();

        LOGGER.debug("Random villain {}", randomVillain);

        return Response.ok(randomVillain).build();
    }

    @GET
    @Operation(summary = "Return all villains")
    @APIResponse(
        responseCode = "200",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(
                implementation = Villain.class,
                type = SchemaType.ARRAY)
        )
    )
    public Response getAllVillains() {
        var villains = this.villainService.findAllVillains();

        LOGGER.debug("Total villains {}", villains.size());

        return Response.ok(villains).build();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Return villain for given ID")
    @APIResponse(
        responseCode = "200",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(
                implementation = Villain.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Villain not found"
    )
    public Response getVillain(@PathParam("id") Long id) {
        var villain = this.villainService.findVillainById(id);

        if (Objects.nonNull(villain)) {
            LOGGER.debug("Found villain {}", villain);
            return Response.ok(villain).build();
        }

        LOGGER.debug("No found villain");
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Operation(summary = "Create villain")
    @APIResponse(
        responseCode = "201",
        description = "URI of created villain",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(
                implementation = URI.class)
        )
    )
    public Response createVillain(@Valid Villain villain, @Context UriInfo uriInfo) {
        var villainCreated = this.villainService.addVillain(villain);

        var uri = uriInfo.getAbsolutePathBuilder()
            .path(Long.toString(villainCreated.id))
            .build();

        LOGGER.debug("Villain created {}", uri.toString());

        return Response.created(uri).build();
    }

    @PUT
    @Operation(summary = "Update villain")
    @APIResponse(
        responseCode = "200",
        description = "The updated villain",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(
                implementation = Villain.class)
        )
    )
    public Response updateVillain(@Valid Villain villain) {

        var villainUpdated = this.villainService.updateVillain(villain);

        LOGGER.debug("Villain updated {}", villainUpdated);

        return Response.ok(villainUpdated).build();
    }

    @DELETE
    @Path("{id}")
    @Operation(summary = "Delete villain for given ID")
    @APIResponse(
        responseCode = "204",
        description = "Villain deleted"
    )
    public Response deleteVillain(@PathParam("id") Long id) {

        this.villainService.deleteVillain(id);

        LOGGER.debug("Villain deleted");

        return Response.noContent().build();
    }
}
