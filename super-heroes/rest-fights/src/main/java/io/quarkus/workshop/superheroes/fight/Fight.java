package io.quarkus.workshop.superheroes.fight;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;

@Entity
@Schema(description = "Informations of winner and loser")
public class Fight extends PanacheEntity {

    @NotNull
    public Instant fightDate;

    @NotNull
    public String winnerName;

    @NotNull
    public int winnerLevel;

    @NotNull
    public String winnerPicture;

    @NotNull
    public String winnerTeam;

    @NotNull
    public String loserName;

    @NotNull
    public int loserLevel;

    @NotNull
    public String loserPicture;

    @NotNull
    public String loserTeam;

    @Override
    public String toString() {
        return "Fight{" +
            "fightDate=" + fightDate +
            ", winnerName='" + winnerName + '\'' +
            ", winnerLevel=" + winnerLevel +
            ", winnerPicture='" + winnerPicture + '\'' +
            ", winnerTeam='" + winnerTeam + '\'' +
            ", loserName='" + loserName + '\'' +
            ", loserLevel=" + loserLevel +
            ", loserPicture='" + loserPicture + '\'' +
            ", loserTeam='" + loserTeam + '\'' +
            ", id=" + id +
            "} " + super.toString();
    }
}
