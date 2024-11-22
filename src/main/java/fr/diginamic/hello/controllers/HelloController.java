package fr.diginamic.hello.controllers;

import fr.diginamic.hello.services.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// mapping de l'url qui permettra à l'API de joindre ce contrôleur
@RequestMapping("/hello")
public class HelloController {

    // injection de dépendances : bean Spring du service créé
    // et géré dans le conteneur IoC Spring
    // ==> pas besoin d'initialiser nous-mêmes le service
    @Autowired
    private HelloService helloService;

    @GetMapping
    public String sayHello() {
        return helloService.salutations();
    }
}
