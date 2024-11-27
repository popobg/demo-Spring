package fr.diginamic.hello.services;

import fr.diginamic.hello.repositories.DepartementDao;
import fr.diginamic.hello.httpStatusCode.EnumHttpStatus;
import fr.diginamic.hello.models.Departement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Classe service de traitement des requêtes du controller au repository des départements
 */
@Service
public class DepartementService {

    /** Repository contenant les données liées aux départements */
    @Autowired
    private DepartementDao departementDao;

    /**
     * Méthode permettant de demander des départements au repository.
     * @return départements
     */
    public List<Departement> getDepartements() {
        return departementDao.extractDepartements();
    }

    /**
     * Méthode permettant de demander un département au repository à partir de son id.
     * @return département
     */
    public Departement getDepartementById(long id) {
        return departementDao.extractDepartementById(id);
    }

    /**
     * Méthode permettant de demander un département au repository à partir de son nom.
     * @return département
     */
    public Departement getDepartementByCode(String code) {
        return departementDao.extractDepartementByCode(code);
    }

    /**
     * Méthode permettant de donner un département au repository à ajouter en base de données.
     * @param dept département à ajouter
     */
    public void insertDepartement(Departement dept) {
        departementDao.insertDepartement(dept);
    }

    /**
     * Méthode récupérant un département existant à partir de son id,
     * et mettant-à-jour ses informations.
     * Passage ensuite au repository pour persistance en base de données.
     * @param id identifiant du département à modifier
     * @param dept objet département contenant les nouvelles informations
     * @return un enum reflétant le statut de la requête
     */
    public EnumHttpStatus updateDepartement(long id, Departement dept) {
        Departement deptExistant = departementDao.extractDepartementById(id);

        if (deptExistant == null) {
            return EnumHttpStatus.NOTFOUND;
        }

        deptExistant.setNom(dept.getNom());
        deptExistant.setCode(dept.getCode());
        deptExistant.setVilles(dept.getVilles());
        departementDao.updateDepartement(deptExistant);
        return EnumHttpStatus.OK;
    }

    /**
     * Méthode permettant de trouver le département existant à partir de son id,
     * et de le donner au repository pour le supprimer.
     * @param id identifiant du département à supprimer
     * @return un enum reflétant le statut de la requête
     */
    public EnumHttpStatus deleteDepartement(long id) {
        Departement dept = departementDao.extractDepartementById(id);

        if (dept == null) {
            return EnumHttpStatus.NOTFOUND;
        }

        departementDao.deleteDepartement(dept);
        return EnumHttpStatus.OK;
    }
}
