package fr.diginamic.hello.exceptions;

/**
 * Classe Exception customisée en cas de ressource à ajouter déjà présente en base de données
 */
public class RessourceExistanteException extends Exception {
    /**
     * Constructeur
     * @param message message d'erreur
     */
    public RessourceExistanteException(String message) {
        super(message);
    }
}
