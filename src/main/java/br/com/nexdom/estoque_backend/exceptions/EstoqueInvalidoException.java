package br.com.nexdom.estoque_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
public class EstoqueInvalidoException extends RuntimeException {
    public EstoqueInvalidoException(String message) {
        super(message);
    }
}
