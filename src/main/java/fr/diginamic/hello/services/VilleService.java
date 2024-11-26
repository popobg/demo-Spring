package fr.diginamic.hello.services;

import fr.diginamic.hello.httpStatusCode.EnumHttpStatus;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.dao.VilleDao;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Classe service de traitement des requêtes du controller au repository des villes
 */
@Service
public class VilleService {
    /** Repository contenant les données liées aux villes */
    @Autowired
    private VilleDao villeDao;

    /** Repository contenant les données liées aux départements */
    @Autowired
    private DepartementService deptService;

    /**
     * Intialisation de données dans la table Ville.
     */
    @PostConstruct
    public void init() {
        villeDao.insertVille(new Ville("Nice", 34300, deptService.getDepartementByCode("06")));
        villeDao.insertVille(new Ville("Carcassonne", 47800, deptService.getDepartementByCode("11")));
        villeDao.insertVille(new Ville("Narbonne", 53400, deptService.getDepartementByCode("11")));
        villeDao.insertVille(new Ville("Lyon", 484000, deptService.getDepartementByCode("69D")));
        villeDao.insertVille(new Ville("Foix", 9703, deptService.getDepartementByCode("09")));
        villeDao.insertVille(new Ville("Pau", 77200, deptService.getDepartementByCode("64")));
        villeDao.insertVille(new Ville("Marseille", 850700, deptService.getDepartementByCode("13")));
        villeDao.insertVille(new Ville("St-Cyr-sur-Mer", 35000, deptService.getDepartementByCode("13")));
        villeDao.insertVille(new Ville("Tarbes", 40600, deptService.getDepartementByCode("65")));
        villeDao.insertVille(new Ville("Castres", 40000, deptService.getDepartementByCode("65")));
    }

    /**
     * Méthode permettant de récupérer des villes au repository.
     * @return villes
     */
    public List<Ville> getVilles() {
        return villeDao.extractVilles();
    }

    /**
     * Méthode permettant de demander une ville au repository à partir de son id.
     * @return ville
     */
    public Ville getVilleById(long id) {
        return villeDao.extractVilleById(id);
    }

    /**
     * Méthode permettant de demander une ville au repository à partir de son nom.
     * @return ville
     */
    public Ville getVilleByName(String nom) {
        return villeDao.extractVilleByName(nom);
    }

    /**
     * Méthode permettant de donner une ville au repository à ajouter en base de données.
     * @param ville ville à ajouter
     */
    public void insertVille(Ville ville) {
        villeDao.insertVille(ville);
    }

    /**
     * Méthode récupérant une ville existante à partir de son id,
     * et mettant-à-jour ses informations.
     * Passage ensuite au repository pour persistance en base de données.
     * @param id identifiant de la ville à modifier
     * @param ville objet ville contenant les nouvelles informations
     * @return un enum reflétant le statut de la requête
     */
    public EnumHttpStatus updateVille(long id, Ville ville) {
        Ville villeExistante = villeDao.extractVilleById(id);

        if (villeExistante == null) {
            return EnumHttpStatus.NOTFOUND;
        }

        villeExistante.setNom(ville.getNom());
        villeExistante.setNbHabitants(ville.getNbHabitants());
        villeDao.updateVille(villeExistante);
        return EnumHttpStatus.OK;
    }

    /**
     * Méthode permettant de trouver une ville existante à partir de son id,
     * et de le donner au repository pour le supprimer.
     * @param id identifiant de la ville à supprimer
     * @return un enum reflétant le statut de la requête
     */
    public EnumHttpStatus deleteVille(long id) {
        Ville ville = villeDao.extractVilleById(id);

        if (ville == null) {
            return EnumHttpStatus.NOTFOUND;
        }

        villeDao.deleteVille(ville);
        return EnumHttpStatus.OK;
    }

    /**
     * Demande au repository la liste des villes triées par département et habitants.
     * @param codeDep code du département
     * @param n nombre d'habitants
     * @return liste de villes
     */
    public List<Ville> findByDepartementCodeOrderByNbHabDesc(String codeDep, int n) {
        return villeDao.findByDepartementCodeOrderByNbHabDesc(codeDep, n);
    }

    /**
     * Demande au repository la liste des N villes triées par code de départements
     * et nombre d'habitants.
     * @param codeDep code du département
     * @param min nombre minimum d'habitants
     * @param max nombre maximum d'habitants
     * @return liste de villes
     */
    public List<Ville> findByDepartementCodeAndNbHabBetween(String codeDep, int min, int max) {
        return villeDao.findByDepartementCodeAndNbHabBetween(codeDep, min, max);
    }
}
