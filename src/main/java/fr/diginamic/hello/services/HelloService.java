package fr.diginamic.hello.services;

import org.springframework.stereotype.Service;

/**
 * Classe service gérant les salutations
 */
@Service
public class HelloService {
    /**
     * Retourne une salutation
     * @return String salutation
     */
    public String salutations() {
        return "Je suis la classe de service, et je vous dis 'Bonjour' !";
    }
}
