package io.quarkus.workshop.superheroes.fight.client;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

@Schema(description="Hero fighting against the villain")
public record Hero(
    @NotNull
    String name,
    @NotNull
    int level,
    @NotNull
    String picture,
    String powers) {}
