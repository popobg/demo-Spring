package fr.diginamic.hello.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe entité décrivant une ville (nom et nombre d'habitants)
 */
public class Ville implements Serializable {
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
     * Vérifie si deux objets de la classe Ville sont identiques.
     * @param o instance de classe Object
     * @return boolean, true si les objets sont identiques, sinon false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ville ville)) return false;
        return nbHabitants == ville.nbHabitants && nom.equalsIgnoreCase(ville.nom);
    }

    /**
     * Retourne le hashcode de l'objet Ville.
     * @return entier
     */
    @Override
    public int hashCode() {
        return Objects.hash(nom, nbHabitants);
    }

    /**
     * Retourne les informations générales liées à l'objet Ville.
     * @return String
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ville{");
        sb.append("nom='").append(nom).append('\'');
        sb.append(", nbHabitants=").append(nbHabitants);
        sb.append('}');
        return sb.toString();
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
