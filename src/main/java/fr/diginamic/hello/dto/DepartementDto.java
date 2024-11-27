package fr.diginamic.hello.dto;

import java.io.Serializable;

/**
 * Classe DTO utilisée pour
 */
public class DepartementDto implements Serializable {
    /** code du département */
    private String codeDepartement;
    /** Nom du département */
    private String nomDepartement;
    /** nombre d'habitants du département */
    private int nbHabitants;

    /**
     * Constructeur vide
     */
    public DepartementDto() {
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
