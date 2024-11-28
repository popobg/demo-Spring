package fr.diginamic.hello.restControllers;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.exceptions.RequeteIncorrecteException;
import fr.diginamic.hello.exceptions.RessourceExistanteException;
import fr.diginamic.hello.exceptions.RessourceNotFoundException;
import fr.diginamic.hello.mappers.VilleMapper;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<VilleDto> getVilles() throws RessourceNotFoundException {
        List<Ville> villes = villeService.getVilles();
        return VilleMapper.toDtos(villes);
    }

    /**
     * Méthode permettant de récupérer un ensemble d'objets Ville
     * triés par nom avec une pagination.
     * @param n nombre d'éléments à afficher
     * @return liste de villes
     */
    @GetMapping("/liste/pagination")
    public List<VilleDto> getVilles(@RequestParam int n) throws RessourceNotFoundException, RequeteIncorrecteException {
        List<Ville> villes = villeService.getVillesPagination(n);
        return VilleMapper.toDtos(villes);
    }

    /**
     * Méthode permettant de récupérer une ville à partir de son id.
     * @param id identifiant de la ville
     * @return une ville
     */
    // URL paramétrée
    @GetMapping("/{id}")
    public VilleDto getVilleById(@PathVariable long id) throws RessourceNotFoundException {
        return VilleMapper.toDto(villeService.getVilleById(id));
    }

    /**
     * Méthode permettant de récupérer une ville à partir de son nom.
     * @param nom nom de la ville
     * @return une ville
     */
    @GetMapping("/nom/{nom}")
    public List<VilleDto> getVillesByNom(@PathVariable String nom) throws RequeteIncorrecteException, RessourceNotFoundException {
        List<Ville> villes = villeService.getVillesByNom(nom);
        return VilleMapper.toDtos(villes);
    }

    /**
     * Méthode permettant d'ajouter un objet Ville aux villes enregistrées.
     * @param villeDto objet DTO ville
     * @param result objet injecté par Spring Validation pour vérifier la validité des champs de ville
     * @return une liste de villes et le statut HTTP de la requête accompagné d'un message
     */
    @PostMapping
    public ResponseEntity<List<VilleDto>> addVille(@Valid @RequestBody VilleDto villeDto, BindingResult result) throws RessourceExistanteException, RequeteIncorrecteException, RessourceNotFoundException {
        if (result.hasErrors()) {
            throw new RequeteIncorrecteException(result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }

        villeService.insertVille(VilleMapper.toEntity(villeDto));

        List<Ville> villes = villeService.getVilles();
        return ResponseEntity.ok(VilleMapper.toDtos(villes));
    }

    /**
     * Méthode permettant de modifier les informations d'une ville existante.
     * @param id identifiant de la ville à modifier
     * @param villeDto objet DTO ville contenant les nouvelles informations
     * @return le statut HTTP de la requête accompagné d'un message
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateVille(@PathVariable long id, @Valid @RequestBody VilleDto villeDto, BindingResult result) throws RessourceNotFoundException, RequeteIncorrecteException {
        if (result.hasErrors()) {
            throw new RequeteIncorrecteException(result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }

        villeService.updateVille(id, VilleMapper.toEntity(villeDto));
        return ResponseEntity.ok(String.format("La ville %s a été mise à jour avec succès.", villeDto.getNom()));
    }

    /**
     * Méthode permettant de supprimer une ville à partir de son identifiant.
     * @param id identifiant de la ville
     * @return le statut HTTP de la requête accompagné d'un message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVille(@PathVariable long id) throws RessourceNotFoundException {
        villeService.deleteVille(id);
        return ResponseEntity.ok(String.format("La ville d'id %s a été supprimée avec succès.", id));
    }

    /**
     * Retourne les villes dont le nom commence par le préfixe donné.
     * @param prefixe String
     * @return liste de villes + statut de la requête HTTP
     */
    // requête paramétrée (à la place de l'URL paramétré)
    @GetMapping("/prefixe_nom")
    public List<VilleDto> getVillesByNomStartingWith(@RequestParam String prefixe) throws RessourceNotFoundException {
        List<Ville> villes = villeService.extractVillesByNomStartingWith(prefixe);
        return VilleMapper.toDtos(villes);
    }

    /**
     * Retourne les villes dont le nombre d'habitants est supérieur à un minimum donné.
     * @param min nombre minimum d'habitants
     * @return liste de villes + statut de la requête HTTP
     */
    @GetMapping("/nb_habitants/{min}")
    public List<VilleDto> getVillesByNbHabGreaterThan(@PathVariable int min) throws RessourceNotFoundException {
        List<Ville> villes = villeService.extractVillesByNbHabGreaterThan(min);
        return VilleMapper.toDtos(villes);
    }

    /**
     * Retourne les villes dont le nombre d'habitants est compris dans un intervalle donné.
     * @param min nombre minimum d'habitantes
     * @param max nombre maximum d'habitants
     * @return liste de villes + statut de la requête HTTP
     */
    @GetMapping("/nb_habitants")
    public List<VilleDto> getVillesByNbHabBetween(@RequestParam int min, @RequestParam int max) throws RessourceNotFoundException {
        List<Ville> villes = villeService.extractVillesByNbHabBetween(min, max);
        return VilleMapper.toDtos(villes);
    }

    /**
     * Retourne les villes d'un département dont le nombre d'habitants est supérieur à un certain seuil.
     * @param codeDep code du département
     * @param min nombre minimum d'habitants
     * @return liste de villes + statut de la requête HTTP
     */
    @GetMapping("/DeptAndNbHab/{codeDep}/{min}")
    public List<VilleDto> getVillesByDepartementAndNbHabGreaterThan(@PathVariable String codeDep, @PathVariable int min) throws RessourceNotFoundException {
        List<Ville> villes = villeService.extractVillesByDepartementCodeAndNbHabitantsGreaterThan(codeDep, min);
        return VilleMapper.toDtos(villes);
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
    public List<VilleDto> getVillesByDepartmentCodeAndNbInhabitantsBetween(@PathVariable String codeDep, @PathVariable Integer min, @PathVariable Integer max) throws RessourceNotFoundException {
        List<Ville> villes = villeService.extractVillesByDepartementCodeAndNbHabBetween(codeDep, min,max);
        return VilleMapper.toDtos(villes);
    }

    /**
     * Retourne les N plus grandes villes d'un département.
     * @param codeDep code département
     * @param n nombre d'éléments à afficher
     * @return liste de villes + statut de la requête HTTP
     */
    @GetMapping("/DeptOrderNbHab/{codeDep}")
    public List<VilleDto> getNVillesByDepartmentCodeOrderByNbInhabitantsDesc(@PathVariable String codeDep, @RequestParam Integer n) throws RessourceNotFoundException, RequeteIncorrecteException {
        List<Ville> villes = villeService.extractVillesByDepartementCodeOrderByNbHabDesc(codeDep, n);
        return VilleMapper.toDtos(villes);
    }
}
