package com.wewine.wewine.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Esta anotação diz ao Spring para retornar o status HTTP 404 (Not Found)
// sempre que esta exceção for lançada.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    // Construtor que recebe a mensagem de erro
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Opcional: construtor para receber a mensagem e a causa (stack trace)
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}