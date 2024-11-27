package fr.diginamic.hello.services;

import fr.diginamic.hello.httpStatusCode.EnumHttpStatus;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.repositories.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Classe service de traitement des requêtes du controller au repository des départements
 */
@Service
public class DepartementService {

    /** Repository contenant les données liées aux départements */
    @Autowired
    private DepartementRepository departementRepo;

    /**
     * Demande au repository les départements contenus en base de données.
     * @return liste de départements
     */
    public List<Departement> getDepartements() {
        return departementRepo.findAll();
    }

    /**
     * Demande au repository les départements contenus en base de données, triés par nom.
     * @param pagination paramètres de pagination
     * @return liste de départements
     */
    public List<Departement> getDepartementsPagination(Pageable pagination) {
        return departementRepo.findAllOrderByNom(pagination);
    }

    /**
     * Méthode permettant de demander un département au repository à partir de son id.
     * @return département
     */
    public Departement getDepartementById(long id) {
        Optional<Departement> optDept = departementRepo.findById(id);

        if (optDept.isPresent()) {
            return optDept.get();
        }
        else {
            return null;
        }
    }

    /**
     * Demande un département au repository à partir de son code.
     * @return département
     */
    public Departement getDepartementByCode(String code) {
        Optional<Departement> optDept = departementRepo.findByCode(code);

        if (optDept.isPresent()) {
            return optDept.get();
        }
        else {
            return null;
        }
    }

    /**
     * Méthode permettant de donner un département au repository à ajouter en base de données.
     * @param dept département à ajouter
     */
    public EnumHttpStatus insertDepartement(Departement dept) {
        Optional<Departement> departementExistant = departementRepo.findByCode(dept.getCode());

        if (departementExistant.isPresent()) {
            return EnumHttpStatus.CONFLICT;
        }

        departementRepo.save(dept);
        return EnumHttpStatus.OK;
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
        Optional<Departement> optDept = departementRepo.findById(id);

        if (optDept.isEmpty()) {
            return EnumHttpStatus.NOTFOUND;
        }

        Departement departementExistant = optDept.get();

        departementExistant.setNom(dept.getNom());
        departementExistant.setCode(dept.getCode());
        departementExistant.setVilles(dept.getVilles());
        departementRepo.save(departementExistant);
        return EnumHttpStatus.OK;
    }

    /**
     * Méthode permettant de trouver le département existant à partir de son id,
     * et de le donner au repository pour le supprimer.
     * @param id identifiant du département à supprimer
     * @return un enum reflétant le statut de la requête
     */
    public EnumHttpStatus deleteDepartement(long id) {
        Optional<Departement> optDept = departementRepo.findById(id);

        if (optDept.isEmpty()) {
            return EnumHttpStatus.NOTFOUND;
        }

        departementRepo.deleteById(id);
        return EnumHttpStatus.OK;
    }
}
