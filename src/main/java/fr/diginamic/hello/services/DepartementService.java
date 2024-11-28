package fr.diginamic.hello.services;

import fr.diginamic.hello.exceptions.RequeteIncorrecteException;
import fr.diginamic.hello.exceptions.RessourceExistanteException;
import fr.diginamic.hello.exceptions.RessourceNotFoundException;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.repositories.DepartementRepository;
import fr.diginamic.hello.repositories.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    /** Service gérant les opérations liées aux villes */
    @Autowired
    private VilleRepository villeRepo;

    /**
     * Demande au repository les départements contenus en base de données.
     * @return liste de départements
     */
    public List<Departement> getDepartements() throws RessourceNotFoundException {
        List<Departement> departements = departementRepo.findAll();

        if (departements.isEmpty()) {
            throw new RessourceNotFoundException("Aucun département n'a été trouvé.");
        }

        return departements;
    }

    /**
     * Demande au repository les départements contenus en base de données, triés par nom.
     * @param n nombre d'éléments à afficher
     * @return liste de départements
     */
    public List<Departement> getDepartementsPagination(int n) throws RequeteIncorrecteException, RessourceNotFoundException {
        if (n <= 0) {
            throw new RequeteIncorrecteException("Le nombre d'éléments demandé doit être supérieur à 0.");
        }

        Pageable pagination = PageRequest.of(0, n);
        List<Departement> departements = departementRepo.findAllOrderByNom(pagination);

        if (departements.isEmpty()) {
            throw new RessourceNotFoundException("Aucun département n'a été trouvé.");
        }

        return departements;
    }

    /**
     * Méthode permettant de demander un département au repository à partir de son id.
     * @return département
     */
    public Departement getDepartementById(Long id) throws RessourceNotFoundException, RequeteIncorrecteException {
        if (id == null) {
            throw new RequeteIncorrecteException("Il faut renseigner un id.");
        }

        Optional<Departement> optDept = departementRepo.findById(id);

        if (optDept.isPresent()) {
            return optDept.get();
        }
        else {
            throw new RessourceNotFoundException(String.format("Aucun département dont l'Id est %d n'a été trouvé.", id));
        }
    }

    /**
     * Demande un département au repository à partir de son code.
     * @return département
     */
    public Departement getDepartementByCode(String code) throws RessourceNotFoundException, RequeteIncorrecteException {
        if (code == null || code.isEmpty()) {
            throw new RequeteIncorrecteException("Le code du département doit être renseigné.");
        }

        Optional<Departement> optDept = departementRepo.findByCode(code);

        if (optDept.isPresent()) {
            return optDept.get();
        }
        else {
            throw new RessourceNotFoundException(String.format("Aucun département %s n'a été trouvé.", code));
        }
    }

    /**
     * Méthode permettant de donner un département au repository à ajouter en base de données.
     * @param dept département à ajouter
     */
    public void insertDepartement(Departement dept) throws RessourceExistanteException {
        Optional<Departement> departementExistant = departementRepo.findByCode(dept.getCode());

        if (departementExistant.isPresent()) {
            throw new RessourceExistanteException(String.format("Un département de code %s existe déjà.", dept.getCode()));
        }

        departementRepo.save(dept);
    }

    /**
     * Méthode récupérant un département existant à partir de son id,
     * et mettant-à-jour ses informations.
     * Passage ensuite au repository pour persistance en base de données.
     * @param id identifiant du département à modifier
     * @param dept objet département contenant les nouvelles informations
     */
    public void updateDepartement(Long id, Departement dept) throws RessourceNotFoundException, RequeteIncorrecteException {
        if (id == null) {
            throw new RequeteIncorrecteException("Il faut renseigner un id.");
        }

        Optional<Departement> optDept = departementRepo.findById(id);

        if (optDept.isEmpty()) {
            throw new RessourceNotFoundException(String.format("Aucun département dont l'Id est %d n'a été trouvé.", id));
        }

        Departement departementExistant = optDept.get();

        departementExistant.setNom(dept.getNom());
        departementExistant.setCode(dept.getCode());
        departementExistant.setVilles(dept.getVilles());
        departementRepo.save(departementExistant);
    }

    /**
     * Méthode permettant de trouver le département existant à partir de son id,
     * et de le donner au repository pour le supprimer.
     * @param id identifiant du département à supprimer
     */
    public void deleteDepartement(Long id) throws RessourceNotFoundException, RequeteIncorrecteException {
        if (id == null) {
            throw new RequeteIncorrecteException("Il faut renseigner un id.");
        }

        Optional<Departement> optDept = departementRepo.findById(id);

        if (optDept.isEmpty()) {
            throw new RessourceNotFoundException(String.format("Aucun département dont l'Id est %d n'a été trouvé.", id));
        }

        // Gère la relation one-to-many : si on supprime le département,
        // les villes liées sont supprimées aussi
        for (Ville ville : optDept.get().getVilles()) {
            villeRepo.deleteById(ville.getId());
        }

        departementRepo.deleteById(id);
    }
}
