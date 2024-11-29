package fr.diginamic.hello.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="departement")
public class Departement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    /** Nom de la ville */
    @NotNull(message = "Le nom est obligatoire.")
    @NotEmpty(message = "Le nom est obligatoire.")
    @NotBlank(message = "Le nom est obligatoire.")
    @Size(min = 3, message = "Le nom du département doit comporter au moins trois caractères.")
    @Column(name="NOM")
    private String nom;

    @Column(name="CODE", unique = true)
    @Size(min = 2, max = 3, message = "Le code département doit contenir 2 à 3 caractères.")
    private String code;

    // évite la boucle infinie à la création des objets
    @JsonIgnore
    @OneToMany(mappedBy="departement")
    private List<Ville> villes;

    {
        villes = new ArrayList<>();
    }

    /**
     * Constructeur vide
     */
    public Departement() {
    }

    /**
     * Constructeur
     * @param nom nom du département
     * @param code code du département
     */
    public Departement(String nom, String code) {
        this(0, nom, code);
    }

    public Departement(long id, String nom, String code) {
        this.id = id;
        this.nom = nom;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Departement that)) return false;
        return id == that.id && Objects.equals(nom, that.nom) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, code, villes);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Departement{");
        sb.append("id=").append(id);
        sb.append(", nom='").append(nom).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append(", villes=").append(villes);
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
    public List<Ville> getVilles() {
        return villes;
    }

    /**
     * Setter
     * @param villes villes
     */
    public void setVilles(List<Ville> villes) {
        this.villes = villes;
    }
}
