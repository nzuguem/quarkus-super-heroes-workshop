package io.quarkus.workshop.superheroes.fight.client;

import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Mock
@RestClient
@ApplicationScoped
public class StubVillainProxy implements VillainProxy {

    @Override
    public Villain findRandomVillain() {
        return new Villain(
            "Super Chocolatine",
            42,
            "super_chocolatine.png",
            "does not eat pain au chocolat"
        );
    }
}
