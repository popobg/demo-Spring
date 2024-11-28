package fr.diginamic.hello.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Classe de gestion des exceptions customisée, gérant les exceptions remontées par le contrôleur
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Méthode de gestion de l'exception RessourceNotFound, en cas
     * de ressource non trouvée en base de données.
     * @param ex objet Exception customisée
     * @return un statut HTTP associé au message d'erreur
     */
    @ExceptionHandler(value = {RessourceNotFoundException.class})
    public ResponseEntity<String> handleRessourceNotFoundException(RessourceNotFoundException ex) {
        // Erreur 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(ex.getMessage()));
    }

    /**
     * Méthode de gestion de l'exception RessourceExistante, en cas
     * d'ajout d'une ressource déjà existante en base de données.
     * @param ex objet Exception customisée
     * @return un statut HTTP associé au message d'erreur
     */
    @ExceptionHandler(value = {RessourceExistanteException.class})
    public ResponseEntity<String> handleRessourceExistanteException(RessourceExistanteException ex) {
        // Erreur 409
        return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format(ex.getMessage()));
    }


    /**
     * Méthode de gestion de l'exception RequeteIncorrecte, en cas
     * de requête client incorrecte.
     * @param ex objet Exception customisée
     * @return un statut HTTP associé au message d'erreur
     */
    @ExceptionHandler(value = {RequeteIncorrecteException.class})
    public ResponseEntity<String> handleRequeteIncorrecteException(RequeteIncorrecteException ex) {
        // Erreur 400
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format(ex.getMessage()));
    }
}
