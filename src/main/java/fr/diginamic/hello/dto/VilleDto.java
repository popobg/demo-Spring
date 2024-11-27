package fr.diginamic.hello.dto;

import java.io.Serializable;

/**
 * Classe DTO utilisée pour sérialiser/désérialiser les données en entités JPA.
 */
public class VilleDto implements Serializable {
    /** Nom de la ville */
    private String nom;
    /** Nombre d'habitants */
    private int NbHabitants;
    /** Code du département */
    private String codeDepartement;
    /** Nom du département */
    private String nomDepartement;

    /**
     * Constructeur vide
     */
    public VilleDto() {
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
     * @return NbHabitants
     */
    public int getNbHabitants() {
        return NbHabitants;
    }

    /**
     * Setter
     * @param nbHabitants NbHabitants
     */
    public void setNbHabitants(int nbHabitants) {
        NbHabitants = nbHabitants;
    }

    /**
     * Getter
     * @return codeDepartement
     */
    public String getCodeDepartement() {
        return codeDepartement;
    }

    /**
     * Setter
     * @param codeDepartement codeDepartement
     */
    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    /**
     * Getter
     * @return nomDepartement
     */
    public String getNomDepartement() {
        return nomDepartement;
    }

    /**
     * Setter
     * @param nomDepartement nomDepartement
     */
    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }
}
