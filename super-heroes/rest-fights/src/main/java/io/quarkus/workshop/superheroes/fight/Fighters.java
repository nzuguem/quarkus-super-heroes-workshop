package io.quarkus.workshop.superheroes.fight;

import io.quarkus.workshop.superheroes.fight.client.Hero;
import io.quarkus.workshop.superheroes.fight.client.Villain;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Fight between one superheroes and one villain")
public record Fighters(
    @NotNull
    Hero hero,

    @NotNull
    Villain villain
) {

    public static Fighters of(Hero hero, Villain villain) {
        return new Fighters(hero, villain);
    }

    public Fight toFightInFavorOfHero() {
        var fight = new Fight();
        fight.winnerName = hero.name();
        fight.winnerPicture = hero.picture();
        fight.winnerLevel = hero.level();
        fight.loserName = villain.name();
        fight.loserPicture = villain.picture();
        fight.loserLevel = villain.level();
        fight.winnerTeam = "heroes";
        fight.loserTeam = "villains";
        return fight;
    }

    public Fight toFightInFavorOfVillain() {
        var fight = new Fight();
        fight.winnerName = villain.name();
        fight.winnerPicture = villain.picture();
        fight.winnerLevel = villain.level();
        fight.loserName = hero.name();
        fight.loserPicture = hero.picture();
        fight.loserLevel = hero.level();
        fight.winnerTeam = "villains";
        fight.loserTeam = "heroes";
        return fight;
    }
}
