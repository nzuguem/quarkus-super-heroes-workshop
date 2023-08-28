package io.quarkus.workshop.superheroes.statistics;

public class TeamStats {
    private int villains = 0;
    private int heroes = 0;

    double add(Fight result) {
        if (result.winnerTeam().equalsIgnoreCase("heroes")) {
            heroes++;
        } else {
            villains++;
        }
        return ((double) heroes / (heroes + villains));
    }
}
