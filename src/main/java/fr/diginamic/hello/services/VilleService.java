package fr.diginamic.hello.services;

import fr.diginamic.hello.httpStatusCode.EnumHttpStatus;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.repositories.Irepositories.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Classe service de traitement des requêtes du controller au repository des villes
 */
@Service
public class VilleService {
    /** Repository contenant les données liées aux villes */
    @Autowired
    private VilleRepository villeRepo;

    /**
     * Méthode permettant de récupérer des villes du repository.
     * @return villes
     */
    public List<Ville> getVilles() {
        return villeRepo.findAll();
    }

    public List<Ville> getVillesPagination(Pageable pagination) {
        return villeRepo.findAllOrderByNom(pagination);
    }

    /**
     * Méthode permettant de demander une ville au repository à partir de son id.
     * @return ville
     */
    public Ville getVilleById(long id) {
        Optional<Ville> optVille = villeRepo.findById(id);

        if (optVille.isPresent()) {
            return optVille.get();
        }
        else {
            return null;
        }
    }

    /**
     * Méthode permettant de demander une ville au repository à partir de son nom.
     * @return ville
     */
    public List<Ville> getVilleByNom(String nom) {
        return villeRepo.findByNom(nom);
    }

    /**
     * Méthode permettant de donner une ville au repository à ajouter en base de données.
     * @param ville ville à ajouter
     */
    public EnumHttpStatus insertVille(Ville ville) {
        List<Ville> villesHomonymes = villeRepo.findByNom(ville.getNom());

        // check si une ville identique existe déjà
        for (Ville v : villesHomonymes) {
            if (v.getNom().equals(ville.getNom())
            && v.getNbHabitants() == ville.getNbHabitants()
            && v.getDepartement().getId() == (ville.getDepartement().getId())) {
                return EnumHttpStatus.CONFLICT;
            }
        }

        villeRepo.save(ville);
        return EnumHttpStatus.OK;
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
        Optional<Ville> optVille = villeRepo.findById(id);

        if (optVille.isEmpty()) {
            return EnumHttpStatus.NOTFOUND;
        }

        Ville villeExistante = optVille.get();
        villeExistante.setNom(ville.getNom());
        villeExistante.setNbHabitants(ville.getNbHabitants());

        if (!villeExistante.getDepartement().equals(ville.getDepartement())) {
            villeExistante.setDepartement(ville.getDepartement());
        }
        villeRepo.save(villeExistante);
        return EnumHttpStatus.OK;
    }

    /**
     * Méthode permettant de trouver une ville existante à partir de son id,
     * et de le donner au repository pour le supprimer.
     * @param id identifiant de la ville à supprimer
     * @return un enum reflétant le statut de la requête
     */
    public EnumHttpStatus deleteVille(long id) {
        Optional<Ville> optVille = villeRepo.findById(id);

        if (optVille.isEmpty()) {
            return EnumHttpStatus.NOTFOUND;
        }

        villeRepo.deleteById(id);
        return EnumHttpStatus.OK;
    }

    /**
     * Demande au repository la liste des villes dont le nom commence par le préfixe donné.
     * @param prefixe String
     * @return liste de villes
     */
    public List<Ville> extractVillesByNomStartingWith(String prefixe) {
        return villeRepo.findByNomStartingWith(prefixe);
    }

    /**
     * Demande au repository la liste des villes dont le nombre d'habitants
     * est supérieur à un seuil donné.
     * @param minHab nombre minimum d'habitants
     * @return liste de villes
     */
    public List<Ville> extractVillesByNbHabGreaterThan(int minHab) {
        return villeRepo.findByNbHabitantsGreaterThan(minHab);
    }

    /**
     * Demande au repository la liste des N plus grandes villes d'un département.
     * @param codeDep code du département
     * @param pagination page et nombre d'éléments
     * @return liste de villes
     */
    public List<Ville> extractVillesByDepartementCodeOrderByNbHabDesc(String codeDep, Pageable pagination) {
        return villeRepo.findByDepartementCodeOrderByNbHabitantsDesc(codeDep, pagination);
    }

    /**
     * Demande au repository la liste des villes dont le nombre d'habitants est compris
     * dans un intervalle donné.
     * @param minHab nombre minimum d'habitants
     * @param maxHab nombre maximum d'habitants
     * @return liste de villes
     */
    public List<Ville> extractVillesByNbHabBetween(int minHab, int maxHab) {
        return villeRepo.findByNbHabitantsBetween(minHab, maxHab);
    }

    /**
     * Demande au repository la liste des villes d'un département dont le nombre d'habitants
     * est supérieur à un minimum donné.
     * @param departementCode code du département
     * @param minHabitants nombre minimum d'habitants
     * @return liste des villes
     */
    public List<Ville> extractVillesByDepartementCodeAndNbHabitantsGreaterThan(String departementCode, int minHabitants) {
        return villeRepo.findByDepartementCodeAndNbHabitantsGreaterThan(departementCode, minHabitants);
    }

    /**
     * Demande au repository la liste des villes d'un département dont le nombre d'habitants
     * est compris dans un intervalle donné.
     * @param codeDep code du département
     * @param min nombre minimum d'habitants
     * @param max nombre maximum d'habitants
     * @return liste de villes
     */
    public List<Ville> extractVillesByDepartementCodeAndNbHabBetween(String codeDep, int min, int max) {
        return villeRepo.findByDepartementCodeAndNbHabitantsBetween(codeDep, min, max);
    }
}
