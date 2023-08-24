package io.quarkus.workshop.superheroes.villain;


import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class VillainApplicationLifeCycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(VillainApplicationLifeCycle.class);

    void onStart(@Observes StartupEvent startupEvent) {
        LOGGER.info(" __     ___ _ _       _             _    ____ ___ ");
        LOGGER.info(" \\ \\   / (_) | | __ _(_)_ __       / \\  |  _ \\_ _|");
        LOGGER.info("  \\ \\ / /| | | |/ _` | | '_ \\     / _ \\ | |_) | | ");
        LOGGER.info("   \\ V / | | | | (_| | | | | |   / ___ \\|  __/| | ");
        LOGGER.info("    \\_/  |_|_|_|\\__,_|_|_| |_|  /_/   \\_\\_|  |___|");
        LOGGER.info("This application Villain is starting with profile {}", ConfigUtils.getProfiles());
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {
        LOGGER.info("The application Villain is stopping ...");
    }
}
