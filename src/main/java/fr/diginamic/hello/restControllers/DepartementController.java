package fr.diginamic.hello.restControllers;

import fr.diginamic.hello.httpStatusCode.EnumHttpStatus;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.services.DepartementService;
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
 * Classe controller gérant les requêtes liées aux départements
 */
@RestController
@RequestMapping("/api/departements")
public class DepartementController {

    /** Service de gestion des départements */
    @Autowired
    private DepartementService deptService;

    /**
     * Méthode permettant de récupérer un ensemble d'objets Departement.
     * @return liste de départements
     */
    @GetMapping("/liste")
    public List<Departement> getDepartements() {
        return deptService.getDepartements();
    }

    /**
     * Méthode permettant de récupérer un ensemble d'objets Departement
     * triés par nom avec une pagination.
     * @param n nombre d'éléments à afficher
     * @return liste de départements
     */
    @GetMapping("/liste/pagination")
    public List<Departement> getDepartementsPagination(@RequestParam int n) {
        Pageable pagination = PageRequest.of(0, n);
        return deptService.getDepartementsPagination(pagination);
    }

    /**
     * Méthode permettant de récupérer un département à partir de son id.
     * @param id identifiant du département
     * @return un département et le statut HTTP de la requête
     */
    // URL paramétrée
    @GetMapping("/{id}")
    public ResponseEntity<Departement> getDepartementById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Departement dept = deptService.getDepartementById(id);

        if (dept == null) {
            // ressource non trouvée : erreur 404
            return ResponseEntity.notFound().build();
        }
        else {
            // statut http ok + département trouvé
            return ResponseEntity.ok(dept);
        }
    }

    /**
     * Méthode permettant de récupérer un département à partir de son nom.
     * @param code code du département
     * @return un département et le statut HTTP de la requête
     */
    // requête paramétrée
    @GetMapping("/code")
    public ResponseEntity<Departement> getDepartementByCode(@RequestParam String code) {
        // Si la requête n'est pas correcte : erreur 400
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Departement dept = deptService.getDepartementByCode(code);

        if (dept == null) {
            // ressource non trouvée : erreur 404
            return ResponseEntity.notFound().build();
        }
        else {
            // statut http ok + département trouvé
            return ResponseEntity.ok(dept);
        }
    }

    /**
     * Méthode permettant d'ajouter un objet Departement aux départements enregistrés.
     * @param dept département
     * @param result objet injecté par Spring Validation pour vérifier la validité des champs de ville
     * @return une liste de départements et le statut HTTP de la requête accompagné d'un message
     */
    @PostMapping
    public ResponseEntity<List<Departement>> addDepartement(@Valid @RequestBody Departement dept, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        if (deptService.insertDepartement(dept) == EnumHttpStatus.OK) {
            return ResponseEntity.ok(deptService.getDepartements());
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

        }
    }

    /**
     * Méthode permettant de modifier les informations d'un département existant.
     * @param id identifiant du département à modifier
     * @param dept département contenant les nouvelles informations
     * @return le statut HTTP de la requête accompagné d'un message
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateVille(@PathVariable Long id, @Valid @RequestBody Departement dept, BindingResult result) {
        // Permet d'avoir l'ensemble des erreurs
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }

        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (deptService.updateDepartement(id, dept) == EnumHttpStatus.OK) {
            return ResponseEntity.ok(String.format("Le département %s a été mis à jour avec succès.", dept.getNom()));
        }
        else {
            // Erreur 404 not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Le département d'id %s n'existe pas.", id));
        }
    }

    /**
     * Méthode permettant de supprimer un département à partir de son identifiant.
     * @param id identifiant du département
     * @return le statut HTTP de la requête accompagné d'un message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVille(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (deptService.deleteDepartement(id) == EnumHttpStatus.OK) {
            return ResponseEntity.ok(String.format("Le département d'id %s a été supprimé avec succès.", id));
        }
        else {
            // Erreur 404 not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Le département d'id %s n'existe pas.", id));
        }
    }
}
