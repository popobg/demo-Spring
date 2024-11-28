package fr.diginamic.hello.restControllers;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.exceptions.RequeteIncorrecteException;
import fr.diginamic.hello.exceptions.RessourceExistanteException;
import fr.diginamic.hello.exceptions.RessourceNotFoundException;
import fr.diginamic.hello.mappers.VilleMapper;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.VilleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Récupération des villes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un tableau JSON des villes",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Une ressource n'a pas été trouvée")
    })
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
    @Operation(summary = "Récupération des villes avec pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un tableau JSON des villes avec la pagination demandée",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Une ressource n'a pas été trouvée")
    })
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
    @Operation(summary = "Récupération d'une ville à partir de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un objet JSON Ville",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Une ressource n'a pas été trouvée"),
            @ApiResponse(responseCode = "400",
                    description = "Erreur dans les paramètres donnés par le client")
    })
    // URL paramétrée
    @GetMapping("/{id}")
    public VilleDto getVilleById(@PathVariable long id) throws RessourceNotFoundException, RequeteIncorrecteException {
        return VilleMapper.toDto(villeService.getVilleById(id));
    }

    /**
     * Méthode permettant de récupérer une ville à partir de son nom.
     * @param nom nom de la ville
     * @return une ville
     */
    @Operation(summary = "Récupération des villes dont le nom est celui donné en paramètre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un tableau JSON de villes",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Une ressource n'a pas été trouvée"),
            @ApiResponse(responseCode = "400",
                    description = "Erreur dans les paramètres donnés par le client")
    })
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
    @Operation(summary = "Création et ajout d'une ville")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un tableau JSON de villes, ville ajoutée incluse",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Une ressource n'a pas été trouvée"),
            @ApiResponse(responseCode = "409",
                    description = "Une ressource identique existe déjà"),
            @ApiResponse(responseCode = "400",
                    description = "Erreur dans les paramètres donnés par le client")
    })
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
    @Operation(summary = "Mise-à-jour des données d'une ville")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un message informant l'utilisateur que la mise-à-jour a été effectuée",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Une ressource n'a pas été trouvée"),
            @ApiResponse(responseCode = "400",
                    description = "Erreur dans les paramètres donnés par le client")
    })
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
    @Operation(summary = "Suppression d'une ville")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un message informant l'utilisateur que la suppression a été effectuée",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Une ressource n'a pas été trouvée"),
            @ApiResponse(responseCode = "400",
                    description = "Erreur dans les paramètres donnés par le client")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVille(@PathVariable long id) throws RessourceNotFoundException, RequeteIncorrecteException {
        villeService.deleteVille(id);
        return ResponseEntity.ok(String.format("La ville d'id %s a été supprimée avec succès.", id));
    }

    /**
     * Retourne les villes dont le nom commence par le préfixe donné.
     * @param prefixe String
     * @return liste de villes + statut de la requête HTTP
     */
    @Operation(summary = "Récupération des villes commençant par un préfixe donné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un tableau JSON de villes commençant par un préfixe donné",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Une ressource n'a pas été trouvée")
    })
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
    @Operation(summary = "Récupération des villes ayant un nombre d'habitants supérieur à un seuil donné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un tableau JSON de villes dont le nombre d'habitants est supérieur à un seuil donné",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Une ressource n'a pas été trouvée")
    })
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
    @Operation(summary = "Récupération des villes dont le nombre d'habitants est compris dans un intervalle donné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un tableau JSON de villes dont le nombre d'habitants est compris dans un intervalle donné",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Une ressource n'a pas été trouvée")
    })
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
    @Operation(summary = "Récupération des villes d'un département dont le nombre d'habitants est supérieur à un seuil donné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un tableau JSON de villes d'un département dont le nombre d'habitants est supérieur à un seuil donné",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Une ressource n'a pas été trouvée")
    })
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
    @Operation(summary = "Récupération des villes d'un département dont le nombre d'habitants est compris dans un intervalle donné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un tableau JSON de villes d'un département dont le nombre d'habitants est compris dans un intervalle donné",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Une ressource n'a pas été trouvée")
    })
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
    @Operation(summary = "Récupération des N plus grandes villes d'un département")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne un tableau JSON des N plus grandes villes d'un département",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VilleDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Une ressource n'a pas été trouvée")
    })
    @GetMapping("/DeptOrderNbHab/{codeDep}")
    public List<VilleDto> getNVillesByDepartmentCodeOrderByNbInhabitantsDesc(@PathVariable String codeDep, @RequestParam Integer n) throws RessourceNotFoundException, RequeteIncorrecteException {
        List<Ville> villes = villeService.extractVillesByDepartementCodeOrderByNbHabDesc(codeDep, n);
        return VilleMapper.toDtos(villes);
    }
}
