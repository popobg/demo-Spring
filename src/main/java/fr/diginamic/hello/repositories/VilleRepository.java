package fr.diginamic.hello.repositories;

import fr.diginamic.hello.models.Ville;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class VilleRepository {
    /** Set d'objets Ville */
    private final Set<Ville> villes;

    /**
     * Constructeur
     * Initialise la liste statique d'objets Ville à chaque lancement de l'application
     */
    public VilleRepository() {
        this.villes = Set.of(new Ville(343000, "Nice"),
                new Ville(47800, "Carcassonne"),
                new Ville(53400, "Narbonne"),
                new Ville(484000, "Lyon"),
                new Ville(9700, "Foix"),
                new Ville(77200, "Pau"),
                new Ville(850700, "Marseille"),
                new Ville(40600, "Tarbes"));
    }

    /**
     * Méthode retournant le set d'objets Ville
     * @return villes
     */
    public Set<Ville> getVilles() {
        return villes;
    }
}
