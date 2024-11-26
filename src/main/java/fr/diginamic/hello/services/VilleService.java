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

    /**
     * Intialisation de données dans la table Ville.
     */
    @PostConstruct
    public void init() {
        villeDao.insertVille(new Ville("Nice", 34300));
        villeDao.insertVille(new Ville("Carcassonne", 47800));
        villeDao.insertVille(new Ville("Narbonne", 53400));
        villeDao.insertVille(new Ville("Lyon", 484000));
        villeDao.insertVille(new Ville("Foix", 9703));
        villeDao.insertVille(new Ville("Pau", 77200));
        villeDao.insertVille(new Ville("Marseille", 850700));
        villeDao.insertVille(new Ville("Tarbes", 40600));
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
    public Ville getVilleById(int id) {
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
    public EnumHttpStatus updateVille(int id, Ville ville) {
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
    public EnumHttpStatus deleteVille(int id) {
        Ville ville = villeDao.extractVilleById(id);

        if (ville == null) {
            return EnumHttpStatus.NOTFOUND;
        }

        villeDao.deleteVille(ville);
        return EnumHttpStatus.OK;
    }
}
