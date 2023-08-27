package io.quarkus.workshop.superheroes.fight.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/villains")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "rest-villains")
public interface VillainProxy {

    @GET
    @Path("random")
    Villain findRandomVillain();
}
