package br.com.nexdom.estoque_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RecursoEmUsoException extends RuntimeException {
    public RecursoEmUsoException(String message) {
        super(message);
    }
}
