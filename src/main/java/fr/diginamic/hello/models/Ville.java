package fr.diginamic.hello.models;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Classe entité décrivant une ville (nom et nombre d'habitants)
 */
public class Ville implements Serializable {
    /** identifiant unique et non modifiable de la ville */
    @NotNull @NotEmpty @NotBlank
    private final String id;

    /** Nom de la ville */
    @NotNull(message = "Le nom est obligatoire.")
    @NotEmpty(message = "Le nom est obligatoire.")
    @NotBlank(message = "Le nom est obligatoire.")
    @Size(min = 2, message = "Le nom doit comporter au moins deux caractères.")
    private String nom;

    /** Nombre d'habitants dans la ville */
    @NotNull
    @Min(value = 1)
    private int nbHabitants;

    /**
     * Constructeur
     * @param nbHabitants nombre d'habitants
     * @param nom nom de la ville
     */
    public Ville(int nbHabitants, String nom) {
        // génération aléatoire de l'ID à la création de l'instance de ville
        this.id = UUID.randomUUID().toString();
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
     * @return id
     */
    public String getId() {
        return id;
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
