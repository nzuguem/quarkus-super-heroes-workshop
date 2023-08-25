package io.quarkus.workshop.superheroes.fight.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/heroes")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface HeroProxy {

    @GET
    @Path("random")
    Hero findRandomHero();
}
