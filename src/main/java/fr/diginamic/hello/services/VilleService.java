package fr.diginamic.hello.services;

import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.repositories.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Classe service utilisée par le controller pour manipuler les objets Ville
 */
@Service
public class VilleService {
    /** Repository contenant les données liées aux villes */
    @Autowired
    private VilleRepository villeRepository;

    /**
     * Méthode permettant de récupérer les villes d'un repository
     * @return villes
     */
    public Set<Ville> getVilles() {
        return villeRepository.getVilles();
    }
}
