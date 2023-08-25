package io.quarkus.workshop.superheroes.fight.client;

import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description="Villain fighting against the hero")
public record Villain(
    @NotNull
    String name,
    @NotNull
    int level,
    @NotNull
    String picture,
    String powers) {}
