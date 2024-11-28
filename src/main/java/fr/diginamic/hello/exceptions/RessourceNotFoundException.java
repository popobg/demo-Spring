package fr.diginamic.hello.exceptions;

/**
 * Classe Exception customisée en cas de ressource non trouvée en base de données
 */
public class RessourceNotFoundException extends Exception {
    /**
     * Constructeur
     * @param message message d'erreur
     */
    public RessourceNotFoundException(String message) {
        super(message);
    }
}
