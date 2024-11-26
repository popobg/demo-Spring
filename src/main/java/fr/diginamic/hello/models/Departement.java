package fr.diginamic.hello.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="departement")
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    /** Nom de la ville */
    @NotNull(message = "Le nom est obligatoire.")
    @NotEmpty(message = "Le nom est obligatoire.")
    @NotBlank(message = "Le nom est obligatoire.")
    @Size(min = 2, message = "Le nom doit comporter au moins deux caractères.")
    @Column(name="NOM")
    private String nom;

    @Column(name="CODE")
    private String code;

    @OneToMany(mappedBy="departement")
    private Set<Ville> villes;

    {
        villes = new HashSet<Ville>();
    }

    /**
     * Constructeur vide
     */
    public Departement() {
    }

    public Departement(String nom, String code) {
        this.nom = nom;
        this.code = code;
    }

    /**
     * Getter
     * @return id
     */
    public int getId() {
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
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter
     * @param code code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter
     * @return villes
     */
    public Set<Ville> getVilles() {
        return villes;
    }

    /**
     * Setter
     * @param villes villes
     */
    public void setVilles(Set<Ville> villes) {
        this.villes = villes;
    }
}
