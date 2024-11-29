package fr.diginamic.hello.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe entité décrivant une ville (nom et nombre d'habitants)
 */
@Entity
@Table(name="ville")
public class Ville implements Serializable {
    /** identifiant unique et non modifiable de la ville */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    /** Nom de la ville */
    @NotNull(message = "Le nom est obligatoire.")
    @NotEmpty(message = "Le nom est obligatoire.")
    @NotBlank(message = "Le nom est obligatoire.")
    @Size(min = 2, message = "Le nom de la ville doit comporter au moins deux caractères.")
    @Column(name="NOM")
    private String nom;

    /** Nombre d'habitants dans la ville */
    @NotNull(message = "Le nombre d'habitants est obligatoire.")
    @Min(value = 10, message = "La ville doit compter au moins 10 habitants.")
    @Column(name="NB_HABITANTS")
    private int nbHabitants;

    @ManyToOne
    @JoinColumn(name="ID_DEPT", nullable = true)
    private Departement departement;

    /**
     * Constructeur vide
     */
    public Ville() {
    }

    /**
     * Constructeur
     * @param nom nom de la ville
     * @param nbHabitants nombre d'habitants
     */
    public Ville(long id, String nom, int nbHabitants) {
        this(id, nom, nbHabitants, null);
    }

    /**
     * Constructeur
     * @param id identifiant de la ville
     * @param nom nom de la ville
     * @param nbHabitants nombre d'habitants
     * @param departement département de la ville
     */
    public Ville(long id, String nom, int nbHabitants, Departement departement) {
        this.id = id;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
        this.departement = departement;
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
        return id == ville.id && nbHabitants == ville.nbHabitants && Objects.equals(nom, ville.nom) && Objects.equals(departement, ville.departement);
    }

    /**
     * Retourne le hashcode de l'objet Ville.
     * @return entier
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, nom, nbHabitants);
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
    public long getId() {
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

    /**
     * Getter
     * @return departement
     */
    public Departement getDepartement() {
        return departement;
    }

    /**
     * Setter
     * @param departement departement
     */
    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
}
