package fr.diginamic.hello.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;

/**
 * Classe DTO utilisée pour sérialiser/désérialiser les données en entités JPA (Ville).
 */
public class VilleDto implements Serializable {
    /** Identifiant de la ville */
    private long id;
    /** Nom de la ville */
    @NotNull
    @Size(min = 2, message = "Le nom de la ville est obligatoire.")
    private String nom;
    /** Nombre d'habitants */
    @NotNull
    @Min(value = 10, message = "La ville doit compter au moins 10 habitants.")
    private int NbHabitants;
    /** Code du département */
    @NotNull
    @Size(min = 2, message = "Le nom du département doit comporter au moins deux caractères.")
    private String codeDepartement;
    /** Nom du département */
    @NotNull
    @Size(min = 2, max = 3, message = "Le code département doit contenir 2 à 3 caractères.")
    private String nomDepartement;

    /**
     * Constructeur vide
     */
    public VilleDto() {
    }

    /**
     * Constructeur
     * @param id identifiant de la ville
     * @param nom nom de la ville
     * @param nbHabitants nombre d'habitants
     * @param codeDepartement code du département
     * @param nomDepartement nom du département
     */
    public VilleDto(long id, String nom, int nbHabitants, String codeDepartement, String nomDepartement) {
        this.id = id;
        this.nom = nom;
        NbHabitants = nbHabitants;
        this.codeDepartement = codeDepartement;
        this.nomDepartement = nomDepartement;
    }

    /**
     * Getter
     * @return Id
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter
     * @param id Id
     */
    public void setId(Long id) {
        this.id = id;
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
