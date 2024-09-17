package br.edu.unicesumar.prova.domain.computador;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ComputadorRepository extends JpaRepository<Computador, UUID> {
}
