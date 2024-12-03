package fr.diginamic.hello.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * MVC Controller
 */
@Controller
public class HelloController {

    /**
     * Méthode très simple permettant de saluer, éventuellement avec un nom donné par le client.
     * @param nom nom passé en paramètre
     * @param model objet contenant les données utilisé par la View
     * @return View
     */
    @GetMapping("/hello")
    public String greeting(@RequestParam(name="nom", required = false, defaultValue="tout le monde") String nom, Model model) {
        model.addAttribute("nom", nom);
        return "hello";
    }
}
