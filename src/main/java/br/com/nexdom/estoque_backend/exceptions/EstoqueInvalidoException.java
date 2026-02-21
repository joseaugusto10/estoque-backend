package br.com.nexdom.estoque_backend.exceptions;

public class EstoqueInvalidoException extends RuntimeException {
    public EstoqueInvalidoException(String message) {
        super(message);
    }
}
