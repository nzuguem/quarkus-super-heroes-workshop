package io.quarkus.workshop.superheroes.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.Cancellable;
import io.smallrye.mutiny.unchecked.Unchecked;
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

@ServerEndpoint("/stats/winners")
@ApplicationScoped
public class TopWinnerWebSocket {

    private final ObjectMapper mapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(TopWinnerWebSocket.class);

    @Channel("winner-stats")
    private Multi<Iterable<Score>> winners;

    private final List<Session> sessions = new CopyOnWriteArrayList<>();
    private Cancellable cancellable;

    public TopWinnerWebSocket(ObjectMapper mapper) {
        this.mapper = mapper;
    }

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
        this.cancellable = this.winners
            .map(Unchecked.function(this.mapper::writeValueAsString))
            .subscribe()
            .with(scores -> sessions.forEach(session -> write(session, scores)));
    }

    @PreDestroy
    public void cleanup() {
        this.cancellable.cancel();
    }

    private void write(Session session, String scores) {
        session.getAsyncRemote().sendText(scores, result -> {
            if (result.getException() != null) {
                LOGGER.error("Unable to write message to web socket", result.getException());
            }
        });
    }
}
