package br.edu.unicesumar.prova.domain.periferico;

import br.edu.unicesumar.prova.domain.Entidade;
import br.edu.unicesumar.prova.domain.computador.Computador;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PERIFERICO")
public class Periferico extends Entidade {
    private String nome;

    @ManyToOne()
    @JoinColumn(name = "COMPUTADOR_ID")
    private Computador computador;

    protected Periferico() {
    }

    public Periferico(String nome, Computador computador) {
        this.nome = nome;
        this.computador = computador;
    }

}
