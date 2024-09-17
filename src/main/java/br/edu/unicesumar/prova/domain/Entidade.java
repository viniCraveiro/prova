package br.edu.unicesumar.prova.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import java.util.UUID;

@MappedSuperclass
public abstract class Entidade implements IEntidade {

    @Id
    @Column(length = 36, nullable = false, updatable = false, unique = true)
    protected UUID id;


    protected Entidade() {
    }

    @PrePersist
    private void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @Override
    public UUID getId() {
        return id;
    }
}
