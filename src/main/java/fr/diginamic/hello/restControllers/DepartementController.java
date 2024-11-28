package fr.diginamic.hello.restControllers;

import fr.diginamic.hello.dto.DepartementDto;
import fr.diginamic.hello.exceptions.RequeteIncorrecteException;
import fr.diginamic.hello.exceptions.RessourceExistanteException;
import fr.diginamic.hello.exceptions.RessourceNotFoundException;
import fr.diginamic.hello.mappers.DepartementMapper;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.services.DepartementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<DepartementDto> getDepartements() throws RessourceNotFoundException {
        List<Departement> departements = deptService.getDepartements();
        return DepartementMapper.toDtos(departements);
    }

    /**
     * Méthode permettant de récupérer un ensemble d'objets Departement
     * triés par nom avec une pagination.
     * @param n nombre d'éléments à afficher
     * @return liste de départements
     */
    @GetMapping("/liste/pagination")
    public List<DepartementDto> getDepartementsPagination(@RequestParam int n) throws RequeteIncorrecteException, RessourceNotFoundException {
        List<Departement> departements = deptService.getDepartementsPagination(n);
        return DepartementMapper.toDtos(departements);
    }

    /**
     * Méthode permettant de récupérer un département à partir de son id.
     * @param id identifiant du département
     * @return un département et le statut HTTP de la requête
     */
    // URL paramétrée
    @GetMapping("/{id}")
    public DepartementDto getDepartementById(@PathVariable Long id) throws RessourceNotFoundException, RequeteIncorrecteException {
        Departement dept = deptService.getDepartementById(id);
        return DepartementMapper.toDto(dept);
    }

    /**
     * Méthode permettant de récupérer un département à partir de son nom.
     * @param code code du département
     * @return un département et le statut HTTP de la requête
     */
    // requête paramétrée
    @GetMapping("/code")
    public DepartementDto getDepartementByCode(@RequestParam String code) throws RessourceNotFoundException, RequeteIncorrecteException {
        Departement dept = deptService.getDepartementByCode(code);
        return DepartementMapper.toDto(dept);
    }

    /**
     * Méthode permettant d'ajouter un objet Departement aux départements enregistrés.
     * @param dept département
     * @param result objet injecté par Spring Validation pour vérifier la validité des champs de VilleDTO
     * @return une liste de départements et le statut HTTP de la requête accompagné d'un message
     */
    @PostMapping
    public ResponseEntity<List<DepartementDto>> addDepartement(@Valid @RequestBody DepartementDto dept, BindingResult result) throws RessourceNotFoundException, RessourceExistanteException, RequeteIncorrecteException {
        if (result.hasErrors()) {
            throw new RequeteIncorrecteException(result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }

        deptService.insertDepartement(DepartementMapper.toEntity(dept));

        List<Departement> departements = deptService.getDepartements();
        return ResponseEntity.ok(DepartementMapper.toDtos(departements));
    }

    /**
     * Méthode permettant de modifier les informations d'un département existant.
     * @param id identifiant du département à modifier
     * @param dept département contenant les nouvelles informations
     * @param result objet injecté par Spring Validation pour vérifier la validité des champs de VilleDTO
     * @return le statut HTTP de la requête accompagné d'un message
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateVille(@PathVariable Long id, @Valid @RequestBody DepartementDto dept, BindingResult result) throws RessourceNotFoundException, RequeteIncorrecteException {
        if (result.hasErrors()) {
            // Permet de visualiser l'ensemble des erreurs de correspondance au DTO
            return ResponseEntity.badRequest().body(result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }

        deptService.updateDepartement(id, DepartementMapper.toEntity(dept));
        return ResponseEntity.ok(String.format("Le département %s a été mis à jour avec succès.", dept.getNomDepartement()));
    }

    /**
     * Méthode permettant de supprimer un département à partir de son identifiant.
     * @param id identifiant du département
     * @return le statut HTTP de la requête accompagné d'un message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVille(@PathVariable Long id) throws RessourceNotFoundException, RequeteIncorrecteException {
        deptService.deleteDepartement(id);
        return ResponseEntity.ok(String.format("Le département d'id %s a été supprimé avec succès.", id));
    }
}
