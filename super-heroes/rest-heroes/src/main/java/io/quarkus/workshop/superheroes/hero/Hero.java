package io.quarkus.workshop.superheroes.hero;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Random;

@Entity
public class Hero extends PanacheEntity {

    @NotNull
    @Size(min = 3, max = 50)
    public String name;

    public String otherName;

    @NotNull
    @Min(1)
    public int level;

    public String picture;

    @Column(columnDefinition = "TEXT")
    public String powers;

    public static Uni<Hero> findRandom() {
        return count()
            .map(count -> new Random().nextInt(count.intValue()))
            .chain(randomIndex -> findAll().page(randomIndex, 1).firstResult());
    }

    @Override
    public String toString() {
        return "Hero{" +
            "name='" + name + '\'' +
            ", otherName='" + otherName + '\'' +
            ", level=" + level +
            ", picture='" + picture + '\'' +
            ", powers='" + powers + '\'' +
            ", id=" + id +
            "} " + super.toString();
    }

    public void merge(Hero hero) {
        name = hero.name;
        otherName = hero.otherName;
        level = hero.level;
        picture = hero.picture;
        powers = hero.powers;
    }
}
