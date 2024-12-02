package fr.diginamic.hello.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Classe entité JPA Departement représentant un département
 */
@Entity
@Table(name="departement")
public class Departement implements Serializable, Comparable<Departement> {
    /** Identifiant du département */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    /** Nom du département */
    @NotNull(message = "Le nom est obligatoire.")
    @NotEmpty(message = "Le nom est obligatoire.")
    @NotBlank(message = "Le nom est obligatoire.")
    @Size(min = 3, message = "Le nom du département doit comporter au moins trois caractères.")
    @Column(name="NOM")
    private String nom;

    /** Code du département (unique */
    @Column(name="CODE", unique = true)
    @Size(min = 2, max = 3, message = "Le code département doit contenir 2 à 3 caractères.")
    private String code;

    /** Liste de villes du département */
    // évite la boucle infinie à la création des objets
    @JsonIgnore
    @OneToMany(mappedBy="departement", fetch = FetchType.EAGER)
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

    /**
     * Constructeur
     * @param id identifiant du département
     * @param nom nom du département
     * @param code code du département
     */
    public Departement(long id, String nom, String code) {
        this.id = id;
        this.nom = nom;
        this.code = code;
    }

    /**
     * Vérifie si deux objets de la classe Departement sont identiques.
     * @param o instance de l'objet à comparer
     * @return boolean, true si les objets sont identiques, sinon false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Departement that)) return false;
        return id == that.id && Objects.equals(nom, that.nom) && Objects.equals(code, that.code);
    }

    /**
     * Retourn el hashcode de l'objet Departement
     * @return entier
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, nom, code, villes);
    }

    /**
     * Retourne les informations générales liées à l'objet Departement.
     * @return String
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Departement{");
        sb.append("id=").append(id);
        sb.append(", nom='").append(nom).append('\'');
        sb.append(", code='").append(code);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Compare deux départements sur la base de leur nom et détermine leur ordre.
     * @param autreDept le département de comparaison
     * @return int, indiquant si le département doit être classé avant ou après :
     *          0 = même classement, même nom
     *          1 = l'instance actuelle est supérieure au département de comparaison
     *          -1 = l'instance actuelle est inférieure au département de comparaison
     */
    @Override
    public int compareTo(Departement autreDept) {
        Collator collator = Collator.getInstance(Locale.FRANCE);
        return collator.compare(this.nom.toLowerCase(), autreDept.getNom().toLowerCase());
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
