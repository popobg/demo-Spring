package fr.diginamic.hello.models;

/**
 * Classe entité décrivant une ville (nom et nombre d'habitants)
 */
public class Ville {
    /** Nom de la ville */
    private String nom;
    /** Nombre d'habitants dans la ville */
    private int nbHabitants;

    /**
     * Constructeur
     * @param nbHabitants nombre d'habitants
     * @param nom nom de la ville
     */
    public Ville(int nbHabitants, String nom) {
        this.nom = nom;
        this.nbHabitants = nbHabitants;
    }

    /**
     * Getter
     * @return nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter
     * @param nom nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter
     * @return nbHabitants
     */
    public int getNbHabitants() {
        return nbHabitants;
    }

    /**
     * Setter
     * @param nbHabitants nbHabitants
     */
    public void setNbHabitants(int nbHabitants) {
        this.nbHabitants = nbHabitants;
    }
}
