package fr.diginamic.hello.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe Controller gérant les salutations
 */
@RestController
// mapping de l'url qui permettra à l'API de joindre ce contrôleur
@RequestMapping("/hello")
public class HelloControleur {

    /**
     * Méthode de salutation
     * @return String hello, salutation informelle et joyeuse
     */
    @GetMapping
    public String sayHello() {
        return "Hello !";
    }
}
