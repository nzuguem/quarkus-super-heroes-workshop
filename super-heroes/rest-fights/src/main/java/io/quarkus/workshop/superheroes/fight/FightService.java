package io.quarkus.workshop.superheroes.fight;

import io.quarkus.workshop.superheroes.fight.client.Hero;
import io.quarkus.workshop.superheroes.fight.client.HeroProxy;
import io.quarkus.workshop.superheroes.fight.client.Villain;
import io.quarkus.workshop.superheroes.fight.client.VillainProxy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
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

    @Channel("fights")
    private Emitter<Fight> emitter;


    private static final Random RANDOM = new Random();


    public List<Fight> findAllFights() {
        return Fight.listAll();
    }

    public Fight findFightById(Long id) {
        return Fight.findById(id);
    }

    public Fighters findRandomFighters() {
        return Fighters.of(this.findRandomHero(), this.findRandomVillain());
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

        this.emitter.send(fight).toCompletableFuture().join();
        
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

    @Fallback(fallbackMethod = "fallbackRandomHero")
    Hero findRandomHero() {
        return this.heroProxy.findRandomHero();
    }

    @Fallback(fallbackMethod = "fallbackRandomVillain")
    Villain findRandomVillain() {
        return villainProxy.findRandomVillain();
    }

    public Hero fallbackRandomHero() {
        LOGGER.warn("Falling back on Hero");
        return new Hero(
            "Fallback hero",
            1,
            "https://dummyimage.com/280x380/1e8fff/ffffff&text=Fallback+Hero",
            "Fallback hero powers"
        );
    }

    public Villain fallbackRandomVillain() {
        LOGGER.warn("Falling back on Villain");
        return new Villain(
            "Fallback villain",
            42,
            "https://dummyimage.com/280x380/b22222/ffffff&text=Fallback+Villain",
            "Fallback villain powers"
        );
    }
}
