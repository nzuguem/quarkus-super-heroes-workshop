package io.quarkus.workshop.superheroes.statistics;

import java.time.Instant;

public record Fight(
    Instant fightDate,
    String winnerName,
    int winnerLevel,
    String winnerPicture,
    String loserName,
    int loserLevel,
    String loserPicture,
    String winnerTeam,
    String loserTeam
) {
}
