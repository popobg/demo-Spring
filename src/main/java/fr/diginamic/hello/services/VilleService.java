package fr.diginamic.hello.services;

import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.repositories.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * Méthode permettant de récupérer une ville à partir de son nom.
     * @return ville
     */
    public Ville getVille(String nomVille) {
        if (nomVille == null || nomVille.isEmpty()) {
            return null;
        }
        return villeRepository.getVille(nomVille);
    }

    /**
     * Méthode permettant d'ajouter une ville dans les données du repository.
     * @param ville ville
     */
    public ResponseEntity<String> addVille(Ville ville) {
        if (ville == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(String.format("Aucune ville demandée.", ville.getNom()));
        }

        if (villeRepository.addVille(ville)) {
            return ResponseEntity.ok(String.format("La ville %s a été insérée avec succès.", ville.getNom()));
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(String.format("La ville %s existe déjà.", ville.getNom()));
        }
    }
}
