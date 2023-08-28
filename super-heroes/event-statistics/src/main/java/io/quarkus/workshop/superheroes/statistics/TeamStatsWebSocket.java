package io.quarkus.workshop.superheroes.statistics;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.Cancellable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint("/stats/team")
@ApplicationScoped
public class TeamStatsWebSocket {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamStatsWebSocket.class);

    @Channel("team-stats")
    private Multi<Double> stat;

    private final List<Session> sessions = new CopyOnWriteArrayList<>();
    private Cancellable cancellable;

    @OnOpen
    public void onOpen(Session session) {
        this.sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        this.sessions.remove(session);
    }

    @PostConstruct
    public void subscribe() {
        this.cancellable = this.stat.subscribe()
            .with(ratio -> sessions.forEach(session -> write(session, ratio)));
    }

    @PreDestroy
    public void cleanup() {
        this.cancellable.cancel();
    }

    private void write(Session session, double ratio) {
        session.getAsyncRemote().sendText(Double.toString(ratio), result -> {
            if (result.getException() != null) {
                LOGGER.error("Unable to write message to web socket", result.getException());
            }
        });
    }
}
