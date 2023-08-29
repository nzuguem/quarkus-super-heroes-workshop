package io.quarkus.workshop.superheroes.hero.health;

import io.quarkus.workshop.superheroes.hero.HeroResource;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
public class PingHeroResourceHealthCheck implements HealthCheck {

    private final HeroResource heroResource;

    public PingHeroResourceHealthCheck(HeroResource heroResource) {
        this.heroResource = heroResource;
    }

    @Override
    public HealthCheckResponse call() {
        var response = this.heroResource.hello();
        
        return HealthCheckResponse.named("Ping Hero REST Endpoint")
            .withData("Response", response)
            .up()
            .build();

    }
}
