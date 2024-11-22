package fr.diginamic.hello.controllers;

import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Classe Controller gérant les requêtes liées aux villes
 */
@RestController
@RequestMapping("/API/villes")
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
    public Ville getVille(@PathVariable String nomVille) {
        return villeService.getVille(nomVille);
    }

    /**
     * Méthode permettant d'ajouter un objet Ville aux villes enregistrées.
     * @param ville ville
     */
    @PostMapping("ville")
    public ResponseEntity<String> addVille(@RequestBody Ville ville) {
        return villeService.addVille(ville);
    }
}
