package fr.diginamic.hello.services;

import fr.diginamic.hello.exceptions.RequeteIncorrecteException;
import fr.diginamic.hello.exceptions.RessourceExistanteException;
import fr.diginamic.hello.exceptions.RessourceNotFoundException;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.repositories.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe service de traitement des requêtes du controller au repository des villes
 */
@Service
public class VilleService {
    /** Repository contenant les données liées aux villes */
    @Autowired
    private VilleRepository villeRepo;

    /** Service permettant des opérations sur les départements */
    @Autowired
    private DepartementService deptService;

    /**
     * Demande au repository les villes contenues en base de données.
     * @return liste de villes
     */
    public List<Ville> getVilles() throws RessourceNotFoundException {
        List<Ville> villes = villeRepo.findAll();

        if (villes.isEmpty()) {
            throw new RessourceNotFoundException("Aucune ville n'a été trouvée.");
        }

        return villes;
    }

    /**
     * Demande au repository les villes contenues en base de données triées par nom.
     * @param n nombre d'éléments à afficher
     * @return liste de villes
     */
    public List<Ville> getVillesPagination(int n) throws RessourceNotFoundException, RequeteIncorrecteException {
        if (n <= 0) {
            throw new RequeteIncorrecteException("Le nombre d'éléments demandé doit être supérieur à 0.");
        }

        Pageable pagination = PageRequest.of(0, n);
        List<Ville> villes = villeRepo.findAllOrderByNom(pagination);

        if (villes.isEmpty()) {
            throw new RessourceNotFoundException("Aucune ville n'a été trouvée.");
        }

        return villes;
    }

    /**
     * Méthode permettant de demander une ville au repository à partir de son id.
     * @return ville
     */
    public Ville getVilleById(Long id) throws RessourceNotFoundException, RequeteIncorrecteException {
        if (id == null) {
            throw new RequeteIncorrecteException("Il faut renseigner un id.");
        }

        Optional<Ville> optVille = villeRepo.findById(id);

        if (optVille.isPresent()) {
            return optVille.get();
        }
        else {
            throw new RessourceNotFoundException(String.format("Aucune ville d'id %d n'a été trouvée.", id));
        }
    }

    /**
     * Méthode permettant de demander une ville au repository à partir de son nom.
     * @return ville
     */
    public List<Ville> getVillesByNom(String nom) throws RequeteIncorrecteException, RessourceNotFoundException {
        if (nom == null || nom.isEmpty()) {
            throw new RequeteIncorrecteException("Il faut indiquer un nom.");
        }

        List<Ville> villes = villeRepo.findByNom(nom);

        if (villes.isEmpty()) {
            throw new RessourceNotFoundException(String.format("Aucune ville portant le nom %s n'a été trouvée.", nom));
        }

        return villes;
    }

    /**
     * Méthode permettant de donner une ville au repository à ajouter en base de données.
     * @param ville ville à ajouter
     */
    public void insertVille(Ville ville) throws RessourceExistanteException, RequeteIncorrecteException, RessourceNotFoundException {
        List<Ville> villesHomonymes = villeRepo.findByNom(ville.getNom());

        // check si une ville identique existe déjà
        for (Ville v : villesHomonymes) {
            if (v.getNom().equals(ville.getNom())
            && v.getDepartement().getCode().equals(ville.getDepartement().getCode())) {
                throw new RessourceExistanteException(String.format("La ville de nom %s dans le département %s existe déjà.", ville.getNom(), ville.getDepartement().getNom()));
            }
        }

        // Ajoute le département complet à la ville
        Departement departement = deptService.getDepartementByCode(ville.getDepartement().getCode());
        ville.setDepartement(departement);

        villeRepo.save(ville);
    }

    /**
     * Méthode récupérant une ville existante à partir de son id,
     * et mettant-à-jour ses informations.
     * Passage ensuite au repository pour persistance en base de données.
     * @param id identifiant de la ville à modifier
     * @param ville objet ville contenant les nouvelles informations
     */
    public void updateVille(Long id, Ville ville) throws RessourceNotFoundException, RequeteIncorrecteException {
        if (id == null) {
            throw new RequeteIncorrecteException("Il faut renseigner un id.");
        }

        Optional<Ville> optVille = villeRepo.findById(id);

        if (optVille.isEmpty()) {
            throw new RessourceNotFoundException(String.format("La ville d'id %d n'existe pas.", id));
        }

        Ville villeExistante = optVille.get();
        villeExistante.setNom(ville.getNom());
        villeExistante.setNbHabitants(ville.getNbHabitants());

        // Si le département a changé, ajoute le département complet à la ville
        if (!villeExistante.getDepartement().getCode().equals(ville.getDepartement().getCode())) {
            Departement departement = deptService.getDepartementByCode(ville.getDepartement().getCode());
            villeExistante.setDepartement(departement);
        }
        villeRepo.save(villeExistante);
    }

    /**
     * Méthode permettant de trouver une ville existante à partir de son id,
     * et de le donner au repository pour le supprimer.
     * @param id identifiant de la ville à supprimer
     */
    public void deleteVille(Long id) throws RessourceNotFoundException, RequeteIncorrecteException {
        if (id == null) {
            throw new RequeteIncorrecteException("Il faut renseigner un id.");
        }

        Optional<Ville> optVille = villeRepo.findById(id);

        if (optVille.isEmpty()) {
            throw new RessourceNotFoundException(String.format("La ville d'id %d n'existe pas.", id));
        }

        villeRepo.deleteById(id);
    }

    /**
     * Demande au repository la liste des villes dont le nom commence par le préfixe donné.
     * @param prefixe String
     * @return liste de villes
     */
    public List<Ville> extractVillesByNomStartingWith(String prefixe) throws RessourceNotFoundException {
        List<Ville> villes = villeRepo.findByNomStartingWith(prefixe);

        if (villes.isEmpty()) {
            throw new RessourceNotFoundException(String.format("Aucune ville dont le nom commence par %s n'a été trouvée.", prefixe));
        }

        return villes;
    }

    /**
     * Demande au repository la liste des villes dont le nombre d'habitants
     * est supérieur à un seuil donné.
     * @param min nombre minimum d'habitants
     * @return liste de villes
     */
    public List<Ville> extractVillesByNbHabGreaterThan(int min) throws RessourceNotFoundException {
        List<Ville> villes = villeRepo.findByNbHabitantsGreaterThan(min);

        if (villes.isEmpty()) {
            throw new RessourceNotFoundException(String.format("Aucune ville de plus de %d habitants n'a été trouvée.", min));
        }

        return villes;
    }

    /**
     * Demande au repository la liste des N plus grandes villes d'un département.
     * @param codeDep code du département
     * @param n nombre d'éléments à afficher
     * @return liste de villes
     */
    public List<Ville> extractVillesByDepartementCodeOrderByNbHabDesc(String codeDep, int n) throws RessourceNotFoundException, RequeteIncorrecteException {
        if (n <= 0) {
            throw new RequeteIncorrecteException("Le nombre d'éléments demandé doit être supérieur à 0.");
        }

        Pageable pagination = PageRequest.of(0, n);
        List<Ville> villes = villeRepo.findByDepartementCodeOrderByNbHabitantsDesc(codeDep, pagination);

        if (villes.isEmpty()) {
            throw new RessourceNotFoundException(String.format("Aucune ville du département %s n'a été trouvée.", codeDep));
        }

        return villes;
    }

    /**
     * Demande au repository la liste des villes dont le nombre d'habitants est compris
     * dans un intervalle donné.
     * @param min nombre minimum d'habitants
     * @param max nombre maximum d'habitants
     * @return liste de villes
     */
    public List<Ville> extractVillesByNbHabBetween(int min, int max) throws RessourceNotFoundException {
        List<Ville> villes = villeRepo.findByNbHabitantsBetween(min, max);

        if (villes.isEmpty()) {
            throw new RessourceNotFoundException(String.format("Aucune ville dont la population est comprise entre %d et %d n'a été trouvée.", min, max));
        }

        return villes;
    }

    /**
     * Demande au repository la liste des villes d'un département dont le nombre d'habitants
     * est supérieur à un minimum donné.
     * @param codeDep code du département
     * @param min nombre minimum d'habitants
     * @return liste des villes
     */
    public List<Ville> extractVillesByDepartementCodeAndNbHabitantsGreaterThan(String codeDep, int min) throws RessourceNotFoundException {
        List<Ville> villes = villeRepo.findByDepartementCodeAndNbHabitantsGreaterThan(codeDep, min);

        if (villes.isEmpty()) {
            throw new RessourceNotFoundException(String.format("Aucune ville du département %s dont la population est supérieure à %d n'a été trouvée.", codeDep, min));
        }

        return villes;
    }

    /**
     * Demande au repository la liste des villes d'un département dont le nombre d'habitants
     * est compris dans un intervalle donné.
     * @param codeDep code du département
     * @param min nombre minimum d'habitants
     * @param max nombre maximum d'habitants
     * @return liste de villes
     */
    public List<Ville> extractVillesByDepartementCodeAndNbHabBetween(String codeDep, int min, int max) throws RessourceNotFoundException {
//         Ecrire un test qui vérifie que min < max (pareil pour tous les endroits avec min-max)

        List<Ville> villes = villeRepo.findByDepartementCodeAndNbHabitantsBetween(codeDep, min, max);

        if (villes.isEmpty()) {
            throw new RessourceNotFoundException(String.format("Aucune ville du département %s dont la population est comprise entre %d à %d n'a été trouvée.", codeDep, min, max));
        }

        return villes;
    }
}
