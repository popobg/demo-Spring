package fr.diginamic.hello.repositories;

import fr.diginamic.hello.models.Ville;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe repository contenant les données
 * et permettant de faire des requêtes sur celles-ci.
 */
@Repository
public class VilleRepository {
    /** Liste d'objets Ville */
    private ArrayList<Ville> villes;

    /**
     * Constructeur
     * Initialise la liste statique d'objets Ville à chaque lancement de l'application
     */
    public VilleRepository() {
        this.villes = new ArrayList<>();
        Collections.addAll(this.villes, new Ville(343000, "Nice"),
                new Ville(47800, "Carcassonne"),
                new Ville(53400, "Narbonne"),
                new Ville(484000, "Lyon"),
                new Ville(9700, "Foix"),
                new Ville(77200, "Pau"),
                new Ville(850700, "Marseille"),
                new Ville(40600, "Tarbes"));
    }

    /**
     * Méthode retournant la liste d'objets Ville
     * @return villes
     */
    public List<Ville> getVilles() {
        return villes;
    }

    /**
     * Méthode permettant de récupérer une ville à partir de son nom.
     * @return ville
     */
    public Ville getVille(String nomVille) {
        return villes.stream().filter(v -> v.getNom().equalsIgnoreCase(nomVille)).findFirst().orElse(null);
    }

    /**
     * Méthode permettant d'ajouter une ville à la liste de villes
     * @param ville ville
     */
    public boolean addVille(Ville ville) {
        if (villes.stream().noneMatch(v -> v.equals(ville))) {
            villes.add(ville);
            return true;
        }
        return false;
    }
}
