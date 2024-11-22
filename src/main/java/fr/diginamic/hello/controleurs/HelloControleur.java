package fr.diginamic.hello.controleurs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// mapping de l'url qui permettra à l'API de joindre ce contrôleur
@RequestMapping("/hello")
public class HelloControleur {
    @GetMapping
    public String sayHello() {
        return "Hello !";
    }
}
