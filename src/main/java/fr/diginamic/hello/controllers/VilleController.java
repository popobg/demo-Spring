package fr.diginamic.hello.controllers;

import fr.diginamic.hello.httpStatusCode.EnumHttpStatus;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
     * Méthode permettant de récupérer une ville à partir de son id.
     * @return une ville et le statut HTTP de la requête
     */
    // URL paramétrée
    @GetMapping("/{id}")
    public ResponseEntity<Ville> getVille(@PathVariable String id) {
        // Si la requête n'est pas correcte : erreur 400
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Ville ville = villeService.getVille(id);

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
     * @return le statut HTTP de la requête accompagné d'un message
     */
    @PostMapping
    public ResponseEntity<String> addVille(@Valid @RequestBody Ville ville, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().getFirst().getDefaultMessage());
        }

        if (villeService.addVille(ville) == EnumHttpStatus.OK) {
            return ResponseEntity.ok(String.format("La ville %s a été insérée avec succès.", ville.getNom()));
        }
        else {
            // Si la ressource existe déjà : erreur 409
            return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format("La ville %s existe déjà.", ville.getNom()));
        }
    }

    /**
     * Méthode permettant de modifier les informations d'une ville existante.
     * @param id identifiant de la ville à modifier
     * @param ville ville contenant les nouvelles informations
     * @return le statut HTTP de la requête accompagné d'un message
     */
    // Inconvénient avec la méthode de génération de l'ID à la construction utilisée :
    // un nouvel id est créé pour l'objet contenant les modification à apporter à la ville
    @PutMapping("/{id}")
    public ResponseEntity<String> updateVille(@PathVariable String id, @Valid @RequestBody Ville ville, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().getFirst().getDefaultMessage());
        }

        if (villeService.updateVille(id, ville) == EnumHttpStatus.OK) {
            return ResponseEntity.ok(String.format("La ville %s a été mise à jour avec succès.", ville.getNom()));
        }
        else {
            // Erreur 404 not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("La ville d'id %s n'existe pas.", id));
        }
    }

    /**
     * Méthode permettant de supprimer une ville à partir de son identifiant.
     * @param id identifiant de la ville
     * @return le statut HTTP de la requête accompagné d'un message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVille(@PathVariable String id) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().body("Les données de modification sont incorrectes.");
        }

        if (villeService.deleteVille(id) == EnumHttpStatus.OK) {
            return ResponseEntity.ok(String.format("La ville d'id %s a été supprimée avec succès.", id));
        }
        else {
            // Erreur 404 not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("La ville d'id %s n'existe pas.", id));
        }
    }
}
