package fr.diginamic.hello.repositories;

import fr.diginamic.hello.models.Departement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Classe repository communiquant avec la base de données
 */
@Repository
public class DepartementDao {
    /** Instance d'Entity Manager */
    @PersistenceContext
    private EntityManager em;

    /**
     * Méthode retournant la liste d'objets Departement
     * @return départements
     */
    public List<Departement> extractDepartements() {
        try {
            return em.createQuery("select d from Departement d", Departement.class).getResultList();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Méthode permettant de récupérer un département à partir de son id.
     * @return département
     */
    public Departement extractDepartementById(long id) {
        try {
            return em.find(Departement.class, id);
        }
        catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Méthode permettant de récupérer un département à partir de son nom.
     * @return département
     */
    public Departement extractDepartementByCode(String code) {
        try {
            return em.createQuery("select d from Departement d where d.code = :code", Departement.class).setParameter("code", code).getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Méthode permettant d'ajouter un département à la table Departement.
     * @param departement département
     */
    @Transactional
    public void insertDepartement(Departement departement) {
        em.persist(departement);
    }

    /**
     * Méthode permettant de mettre à jour les données d'un département existant.
     * @param dept objet département contenant les nouvelles données
     */
    @Transactional
    public void updateDepartement(Departement dept) {
        em.merge(dept);
    }

    /**
     * Méthode permettant de supprimer un département de la liste des départements.
     * @param dept département à supprimer
     */
    @Transactional
    public void deleteDepartement(Departement dept) {
        em.remove(dept);
    }
}
