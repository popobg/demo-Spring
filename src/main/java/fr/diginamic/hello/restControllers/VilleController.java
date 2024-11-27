package fr.diginamic.hello.restControllers;

import fr.diginamic.hello.httpStatusCode.EnumHttpStatus;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
     * @return liste de villes
     */
    @GetMapping("/liste")
    public List<Ville> getVilles() {
        return villeService.getVilles();
    }

    /**
     * Méthode permettant de récupérer un ensemble d'objets Ville
     * triés par nom avec une pagination.
     * @param n nombre d'éléments à afficher
     * @return liste de villes
     */
    @GetMapping("/liste/pagination")
    public List<Ville> getVilles(@RequestParam int n) {
        Pageable pagination = PageRequest.of(0, n);
        return villeService.getVillesPagination(pagination);
    }

    /**
     * Méthode permettant de récupérer une ville à partir de son id.
     * @param id identifiant de la ville
     * @return une ville et le statut HTTP de la requête
     */
    // URL paramétrée
    @GetMapping("/{id}")
    public ResponseEntity<Ville> getVilleById(@PathVariable long id) {
        Ville ville = villeService.getVilleById(id);

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
     * Méthode permettant de récupérer une ville à partir de son nom.
     * @param nom nom de la ville
     * @return une ville et le statut HTTP de la requête
     */
    @GetMapping("/nom/{nom}")
    public ResponseEntity<List<Ville>> getVilleByNom(@PathVariable String nom) {
        // Si la requête n'est pas correcte : erreur 400
        if (nom == null || nom.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(villeService.getVilleByNom(nom));
    }

    /**
     * Méthode permettant d'ajouter un objet Ville aux villes enregistrées.
     * @param ville ville
     * @param result objet injecté par Spring Validation pour vérifier la validité des champs de ville
     * @return une liste de villes et le statut HTTP de la requête accompagné d'un message
     */
    @PostMapping
    public ResponseEntity<List<Ville>> addVille(@Valid @RequestBody Ville ville, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        if (villeService.insertVille(ville) == EnumHttpStatus.OK) {
            return ResponseEntity.ok(villeService.getVilles());
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    /**
     * Méthode permettant de modifier les informations d'une ville existante.
     * @param id identifiant de la ville à modifier
     * @param ville ville contenant les nouvelles informations
     * @return le statut HTTP de la requête accompagné d'un message
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateVille(@PathVariable long id, @Valid @RequestBody Ville ville, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
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
    public ResponseEntity<String> deleteVille(@PathVariable long id) {
        if (villeService.deleteVille(id) == EnumHttpStatus.OK) {
            return ResponseEntity.ok(String.format("La ville d'id %s a été supprimée avec succès.", id));
        }
        else {
            // Erreur 404 not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("La ville d'id %s n'existe pas.", id));
        }
    }

    /**
     * Retourne les villes dont le nom commence par le préfixe donné.
     * @param prefixe String
     * @return liste de villes + statut de la requête HTTP
     */
    // requête paramétrée (à la place de l'URL paramétré)
    @GetMapping("/prefixe_nom")
    public ResponseEntity<List<Ville>> getVillesByNomStartingWith(@RequestParam String prefixe) {
        return ResponseEntity.ok(villeService.extractVillesByNomStartingWith(prefixe));
    }

    /**
     * Retourne les villes dont le nombre d'habitants est supérieur à un minimum donné.
     * @param minHab nombre minimum d'habitants
     * @return liste de villes + statut de la requête HTTP
     */
    @GetMapping("/nb_habitants/{minHab}")
    public ResponseEntity<List<Ville>> getVillesByNbHabGreaterThan(@PathVariable int minHab) {
        return ResponseEntity.ok(villeService.extractVillesByNbHabGreaterThan(minHab));
    }

    /**
     * Retourne les villes dont le nombre d'habitants est compris dans un intervalle donné.
     * @param minHab nombre minimum d'habitantes
     * @param maxHab nombre maximum d'habitants
     * @return liste de villes + statut de la requête HTTP
     */
    @GetMapping("/nb_habitants")
    public ResponseEntity<List<Ville>> getVillesByNbHabBetween(@RequestParam int minHab, @RequestParam int maxHab) {
        return ResponseEntity.ok(villeService.extractVillesByNbHabBetween(minHab, maxHab));
    }

    /**
     * Retourne les villes d'un département dont le nombre d'habitants est supérieur à un certain seuil.
     * @param codeDep code du département
     * @param min nombre minimum d'habitants
     * @return liste de villes + statut de la requête HTTP
     */
    @GetMapping("/DeptAndNbHab/{codeDep}/{min}")
    public ResponseEntity<List<Ville>> getVillesByDepartementAndNbHabGreaterThan(@PathVariable String codeDep, @PathVariable int min) {
        return ResponseEntity.ok(villeService.extractVillesByDepartementCodeAndNbHabitantsGreaterThan(codeDep, min));
    }

    /**
     * Retourne les villes d'un département dont le nombre d'habitants
     * est compris dans un intervalle.
     * @param codeDep code du département
     * @param min nombre minimum d'habitants
     * @param max nombre maximum d'habitants
     * @return liste de villes + statut de la requête HTTP
     */
    @GetMapping("/DeptAndNbHab/{codeDep}/{min}/{max}")
    public ResponseEntity<List<Ville>> getVillesByDepartmentCodeAndNbInhabitantsBetween(@PathVariable("codeDep")String codeDep, @PathVariable("min") Integer min, @PathVariable("max") Integer max) {
        return ResponseEntity.ok(villeService.extractVillesByDepartementCodeAndNbHabBetween(codeDep, min,max));
    }

    /**
     * Retourne les N plus grandes villes d'un département.
     * @param codeDep code département
     * @param n nombre d'éléments à afficher
     * @return liste de villes + statut de la requête HTTP
     */
    @GetMapping("/DeptOrderNbHab/{codeDep}")
    public ResponseEntity<List<Ville>> getNVillesByDepartmentCodeOrderByNbInhabitantsDesc(@PathVariable("codeDep")String codeDep, @RequestParam Integer n) {
        Pageable pagination = PageRequest.of(0, n);
        return ResponseEntity.ok(villeService.extractVillesByDepartementCodeOrderByNbHabDesc(codeDep, pagination));
    }
}
