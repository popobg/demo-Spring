package fr.diginamic.hello.exceptions;

/**
 * Classe Exception customisée en cas de requête client incorrecte
 */
public class RequeteIncorrecteException extends Exception {
    /**
     * Constructeur
     * @param message message d'erreur
     */
    public RequeteIncorrecteException(String message) {
        super(message);
    }
}
