package io.quarkus.workshop.superheroes.villain;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

@ApplicationScoped
@Transactional
public class VillainService {

    @ConfigProperty(name = "level.multiplier", defaultValue="1.0")
    private double levelMultiplier;


    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Villain> findAllVillains() {
        return Villain.listAll();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Villain findVillainById(Long id) {
        return Villain.findById(id);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Villain findRandomVillain() {
        return Villain.findRandom();
    }

    public Villain addVillain(@Valid Villain villain) {
        villain.level = (int) Math.round(villain.level * this.levelMultiplier);
        villain.persist();
        return villain;
    }

    public Villain updateVillain(@Valid Villain villain) {

        var existingVillainToOverride = Villain.<Villain>findById(villain.id);

        existingVillainToOverride.merge(villain);

        return existingVillainToOverride;
    }

    public void deleteVillain(Long id) {

        var existingVillainToDelete = Villain.findById(id);

        existingVillainToDelete.delete();
    }


}
