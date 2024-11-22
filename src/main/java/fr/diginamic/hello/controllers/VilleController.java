package fr.diginamic.hello.controllers;

import fr.diginamic.hello.models.Ville;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Controller gérant les requêtes liées aux villes
 */
@RestController
@RequestMapping("/villes")
public class VilleController {

    /**
     * Méthode permettant de récupérer une liste d'objets Ville
     * @return liste de ville
     */
    @GetMapping
    public List<Ville> getVilles() {
        return List.of(new Ville(343000, "Nice"),
                new Ville(47800, "Carcassonne"),
                new Ville(53400, "Narbonne"),
                new Ville(484000, "Lyon"),
                new Ville(9700, "Foix"),
                new Ville(77200, "Pau"),
                new Ville(850700, "Marseille"),
                new Ville(40600, "Tarbes"));
    }
}
