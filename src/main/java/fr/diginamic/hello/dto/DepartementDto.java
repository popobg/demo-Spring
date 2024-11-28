package fr.diginamic.hello.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * Classe DTO utilisée pour
 */
public class DepartementDto implements Serializable {
    /** Identifiant du département */
    private long id;
    /** code du département */
    @NotNull
    @Size(min = 2, max = 3, message = "Le code département doit contenir 2 à 3 caractères.")
    private String codeDepartement;
    /** Nom du département */
    @NotNull
    @Size(min = 2, message = "Le nom du département doit comporter au moins deux caractères.")
    private String nomDepartement;
    /** nombre d'habitants du département */
    private int nbHabitants;

    /**
     * Constructeur vide
     */
    public DepartementDto() {
    }

    /**
     * Constructeur
     * @param id identifiant du département
     * @param codeDepartement code du département
     * @param nomDepartement nom du département
     * @param nbHabitants nombre d'habitants dans le département
     */
    public DepartementDto(long id, String codeDepartement, String nomDepartement, int nbHabitants) {
        this.id = id;
        this.codeDepartement = codeDepartement;
        this.nomDepartement = nomDepartement;
        this.nbHabitants = nbHabitants;
    }

    /**
     * Getter
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Setter
     * @param id id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter
     * @return code du département
     */
    public String getCodeDepartement() {
        return codeDepartement;
    }

    /**
     * Setter
     * @param codeDepartement code du département
     */
    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    /**
     * Getter
     * @return nom du département
     */
    public String getNomDepartement() {
        return nomDepartement;
    }

    /**
     * Setter
     * @param nomDepartement nom du département
     */
    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
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
     * @param nbHabitants nombre d'habitants
     */
    public void setNbHabitants(int nbHabitants) {
        this.nbHabitants = nbHabitants;
    }
}
