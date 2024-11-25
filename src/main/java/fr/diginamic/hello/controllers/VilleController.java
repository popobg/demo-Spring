package fr.diginamic.hello.controllers;

import fr.diginamic.hello.httpStatusCode.EnumHttpStatus;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Classe Controller gérant les requêtes liées aux villes
 */
@RestController
@RequestMapping("/api/villes")
public class VilleController {

    /** Service de gestion des villes */
    @Autowired
    private VilleService villeService;

    /**
     * Méthode permettant de récupérer un ensemble d'objets Ville.
     * @return set de villes
     */
    @GetMapping("/liste")
    public List<Ville> getVilles() {
        return villeService.getVilles();
    }

    /**
     * Méthode permettant de récupérer une ville à partir de son nom.
     * @return ville
     */
    // URL paramétrée
    @GetMapping("/{nomVille}")
    public ResponseEntity<Ville> getVille(@PathVariable String nomVille) {
        // Si la requête n'est pas correcte : erreur 400
        if (nomVille == null || nomVille.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Ville ville = villeService.getVille(nomVille);

        if (ville == null) {
            // ressource non trouvée : erreur 404
            return ResponseEntity.notFound().build();
        }
        else {
            // statut http ok + ville trouvée
            return ResponseEntity.ok(ville);
        }
    }

    /**
     * Méthode permettant d'ajouter un objet Ville aux villes enregistrées.
     * @param ville ville
     */
    @PostMapping
    public ResponseEntity<String> addVille(@RequestBody Ville ville) {
        if (ville == null || ville.getNom() == null) {
            return ResponseEntity.badRequest().body(String.format("Aucune ville demandée.", ville.getNom()));
        }

        if (villeService.addVille(ville) == EnumHttpStatus.OK) {
            return ResponseEntity.ok(String.format("La ville %s a été insérée avec succès.", ville.getNom()));
        }
        else {
            // Si la ressource existe déjà : erreur 409
            return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format("La ville %s existe déjà.", ville.getNom()));
        }
    }
}
