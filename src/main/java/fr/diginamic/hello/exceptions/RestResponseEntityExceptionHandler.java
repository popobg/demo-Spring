package fr.diginamic.hello.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {RessourceNotFoundException.class})
    public ResponseEntity<String> handleRessourceNotFoundException(RessourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(ex.getMessage()));
    }

    @ExceptionHandler(value = {RessourceExistanteException.class})
    public ResponseEntity<String> handleRessourceExistanteException(RessourceExistanteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format(ex.getMessage()));
    }

    @ExceptionHandler(value = {RequeteIncorrecteException.class})
    public ResponseEntity<String> handleRequeteIncorrecteException(RequeteIncorrecteException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format(ex.getMessage()));
    }
}
