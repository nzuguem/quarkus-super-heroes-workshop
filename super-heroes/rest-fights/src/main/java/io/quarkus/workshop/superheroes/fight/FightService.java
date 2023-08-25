package io.quarkus.workshop.superheroes.fight;

import io.quarkus.workshop.superheroes.fight.client.HeroProxy;
import io.quarkus.workshop.superheroes.fight.client.VillainProxy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Random;

@ApplicationScoped
@Transactional(Transactional.TxType.SUPPORTS)
public class FightService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FightService.class);

    @RestClient
    private HeroProxy heroProxy;

    @RestClient
    private VillainProxy villainProxy;

    private static final Random RANDOM = new Random();


    public List<Fight> findAllFights() {
        return Fight.listAll();
    }

    public Fight findFightById(Long id) {
        return Fight.findById(id);
    }

    public Fighters findRandomFighters() {
        return Fighters.of(this.heroProxy.findRandomHero(), this.villainProxy.findRandomVillain());
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Fight createFight(Fighters fighters) {

        Fight fight;

        var heroLevelAdjust = fighters.hero().level() + RANDOM.nextInt(20);
        var villainLevelAdjust = fighters.villain().level() + RANDOM.nextInt(20);

        if (heroLevelAdjust > villainLevelAdjust) {
            fight = this.heroWon(fighters);
        } else if (heroLevelAdjust < villainLevelAdjust) {
            fight = this.villainWon(fighters);
        } else {
            fight = RANDOM.nextBoolean() ? this.heroWon(fighters) : this.villainWon(fighters);
        }

        fight.fightDate = Instant.now(Clock.systemDefaultZone());
        fight.persist();

        return fight;
    }

    private Fight heroWon(Fighters fighters) {
        LOGGER.info("Yes, Hero won 🏆");
        return fighters.toFightInFavorOfHero();
    }

    private Fight villainWon(Fighters fighters) {
        LOGGER.info("Gee, Villain won 🧟‍♀️");
        return fighters.toFightInFavorOfVillain();
    }
}
