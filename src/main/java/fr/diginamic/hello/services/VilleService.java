package fr.diginamic.hello.services;

import fr.diginamic.hello.httpStatusCode.EnumHttpStatus;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.repositories.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Classe service utilisée par le controller pour manipuler les objets Ville
 */
@Service
public class VilleService {
    /** Repository contenant les données liées aux villes */
    @Autowired
    private VilleRepository villeRepository;

    /**
     * Méthode permettant de récupérer les villes d'un repository.
     * @return villes
     */
    public List<Ville> getVilles() {
        return villeRepository.getVilles();
    }

    /**
     * Méthode permettant de récupérer une ville à partir de son id.
     * @return ville
     */
    public Ville getVille(String id) {
        return villeRepository.getVille(id);
    }

    /**
     * Méthode permettant d'ajouter une ville dans les données du repository.
     * @param ville ville à ajouter
     * @return un enum reflétant le statut de la requête
     */
    public EnumHttpStatus addVille(Ville ville) {
        if (villeRepository.addVille(ville)) {
            return EnumHttpStatus.OK;
        }
        else {
            return EnumHttpStatus.CONFLICT;
        }
    }

    /**
     * Méthode permettant de mettre à jour les données d'une ville existante.
     * @param id identifiant de la ville à modifier
     * @param ville objet ville contenant les nouvelles informations
     * @return un enum reflétant le statut de la requête
     */
    public EnumHttpStatus updateVille(String id, Ville ville) {
        Ville villeExistante = getVille(id);

        if (villeExistante == null) {
            return EnumHttpStatus.NOTFOUND;
        }

        villeRepository.updateVille(villeExistante, ville);
        return EnumHttpStatus.OK;
    }

    /**
     * Méthode permettant de supprimer une ville existante à partir de son id.
     * @param id identifiant de la ville à supprimer
     * @return un enum reflétant le statut de la requête
     */
    public EnumHttpStatus deleteVille(String id) {
        Ville ville = getVille(id);

        if (ville == null) {
            return EnumHttpStatus.NOTFOUND;
        }

        villeRepository.deleteVille(ville);
        return EnumHttpStatus.OK;
    }
}
