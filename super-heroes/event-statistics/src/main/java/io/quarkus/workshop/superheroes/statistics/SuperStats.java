package io.quarkus.workshop.superheroes.statistics;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class SuperStats {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuperStats.class);

    private final Ranking topWinners = new Ranking(10);
    private final TeamStats stats = new TeamStats();

    @Incoming("fights")
    @Outgoing("team-stats")
    public Multi<Double> computeTeamStats(Multi<Fight> results) {
        return results
            .onItem().transform(stats::add)
            .invoke(() -> LOGGER.info("Fight received. Computed the team statistics"));
    }

    @Incoming("fights")
    @Outgoing("winner-stats")
    public Multi<Iterable<Score>> computeTopWinners(Multi<Fight> results) {
        return results
            .group().by(Fight::winnerName)
            .onItem().transformToMultiAndMerge(group ->
                group
                    .onItem().scan(Score::new, this::incrementScore))
            .onItem().transform(topWinners::onNewScore)
            .invoke(() -> LOGGER.info("Fight received. Computed the top winners"));
    }

    private Score incrementScore(Score score, Fight fight) {
        score.name = fight.winnerName();
        score.score = score.score + 1;
        return score;
    }
}
