package fr.diginamic.hello.controllers;

import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * Classe Controller gérant les requêtes liées aux villes
 */
@RestController
@RequestMapping("/villes")
public class VilleController {

    /** Service de gestion des villes */
    @Autowired
    private VilleService villeService;

    /**
     * Méthode permettant de récupérer un ensemble d'objets Ville.
     * @return set de villes
     */
    @GetMapping
    public Set<Ville> getVilles() {
        return villeService.getVilles();
    }
}
