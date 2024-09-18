package br.edu.unicesumar.prova.exception;

import jakarta.persistence.EntityNotFoundException;

public class ComputadorNaoEncontradoException extends EntityNotFoundException {
    public ComputadorNaoEncontradoException(String message) {
        super(message);
    }
}
