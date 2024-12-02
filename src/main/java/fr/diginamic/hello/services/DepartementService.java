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
     * Récupère les départements
     * @return liste de départements
     * @throws RessourceNotFoundException aucun département à retourner
     */
    public List<Departement> getDepartements() throws RessourceNotFoundException {
        List<Departement> departements = departementRepo.findAll();

        if (departements.isEmpty()) {
            throw new RessourceNotFoundException("Aucun département n'a été trouvé.");
        }

        return departements;
    }

    /**
     * Récupère les départements triés par nom et paginés.
     * @param n nombre d'objets à afficher sur la page
     * @return liste de départements
     * @throws RessourceNotFoundException aucun département à retourner
     * @throws RequeteIncorrecteException les paramètres reçus en requête sont invalides
     */
    public List<Departement> getDepartementsPagination(int n) throws RessourceNotFoundException, RequeteIncorrecteException {
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
     * Récupère un département à partir de son ID.
     * @param id identifiant du département
     * @return département
     * @throws RessourceNotFoundException le département n'a pas pu être trouvé à partir de l'ID donné
     * @throws RequeteIncorrecteException les paramètres reçus en requête sont invalides
     */
    public Departement getDepartementById(Long id) throws RessourceNotFoundException, RequeteIncorrecteException {
        if (id == null || id < 0) {
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
     * Récupère un département à partir de son code.
     * @param code code du département
     * @return département
     * @throws RessourceNotFoundException le département n'a pas pu être trouvé à partir du code donné
     * @throws RequeteIncorrecteException les paramètres reçus en requête sont invalides
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
     * Ajoute un département.
     * @param dept département à ajouter
     * @throws RessourceExistanteException le département à ajouter existe déjà dans la base de données
     */
    public void insertDepartement(Departement dept) throws RessourceExistanteException {
        Optional<Departement> departementExistant = departementRepo.findByCode(dept.getCode());

        if (departementExistant.isPresent()) {
            throw new RessourceExistanteException(String.format("Un département de code %s existe déjà.", dept.getCode()));
        }

        departementRepo.save(dept);
    }

    /**
     * Met-à-jour les données d'un département existant.
     * @param id identifiant du département à modifier
     * @param dept objet département contenant les nouvelles informations
     * @throws RessourceNotFoundException le département n'a pas pu être trouvé à partir de l'ID donné
     * @throws RequeteIncorrecteException les paramètres reçus en requête sont invalides
     */
    public void updateDepartement(Long id, Departement dept) throws RessourceNotFoundException, RequeteIncorrecteException {
        if (id == null || id < 0) {
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
     * Supprimer un département et les villes de ce département.
     * @param id identifiant du département à supprimer
     * @throws RessourceNotFoundException le département n'a pas pu être trouvé à partir de l'ID donné
     * @throws RequeteIncorrecteException les paramètres reçus en requête sont invalides
     */
    public void deleteDepartement(Long id) throws RessourceNotFoundException, RequeteIncorrecteException {
        if (id == null || id < 0) {
            throw new RequeteIncorrecteException("Il faut renseigner un id.");
        }

        Optional<Departement> optDept = departementRepo.findById(id);

        if (optDept.isEmpty()) {
            throw new RessourceNotFoundException(String.format("Aucun département dont l'Id est %d n'a été trouvé.", id));
        }

        Departement departement = optDept.get();

        // Gère la relation one-to-many : si on supprime le département,
        // les villes liées sont supprimées aussi
        if (departement.getVilles() != null) {
            for (Ville ville : departement.getVilles()) {
                villeRepo.deleteById(ville.getId());
            }
        }

        departementRepo.deleteById(id);
    }
}
