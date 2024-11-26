package fr.diginamic.hello.dao;

import fr.diginamic.hello.models.Ville;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Classe repository communiquant avec la base de données.
 */
@Repository
public class VilleDao {
    /** Instance d'Entity Manager */
    @PersistenceContext
    private EntityManager em;

    /**
     * Méthode retournant la liste d'objets Ville
     * @return villes
     */
    public List<Ville> extractVilles() {
        try {
            return em.createQuery("select v from Ville v", Ville.class).getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Méthode permettant de récupérer une ville à partir de son id.
     * @return ville
     */
    public Ville extractVilleById(int id) {
        try {
            return em.find(Ville.class, id);
        }
        catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Méthode permettant de récupérer une ville à partir de son nom.
     * @return ville
     */
    public Ville extractVilleByName(String nom) {
        try {
            return (Ville) em.createQuery("select v from Ville v where v.nom = :nom").setParameter("nom", nom).getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Méthode permettant d'ajouter une ville à la liste de villes.
     * @param ville ville
     */
    @Transactional
    public void insertVille(Ville ville) {
        em.persist(ville);
    }

    /**
     * Méthode permettant de mettre à jour les données d'une ville existante.
     * @param ville objet ville contenant les nouvelles données
     */
    @Transactional
    public void updateVille(Ville ville) {
        em.merge(ville);
    }

    /**
     * Méthode permettant de supprimer une ville de la liste des villes.
     * @param ville ville à supprimer
     */
    @Transactional
    public void deleteVille(Ville ville) {
        em.remove(ville);
    }
}
