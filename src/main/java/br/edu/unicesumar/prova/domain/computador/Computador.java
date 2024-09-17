package br.edu.unicesumar.prova.domain.computador;

import br.edu.unicesumar.prova.domain.Entidade;
import br.edu.unicesumar.prova.domain.periferico.Periferico;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "COMPUTADOR")
public class Computador extends Entidade {
    @Column
    private String nome;
    @Column
    private String cor;
    @Setter
    @Column
    private Long dataFabricacao;

    @OneToMany(mappedBy = "computador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Periferico> perifericos = new HashSet<>();

    public Computador() {
    }

    public Computador(Computador entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.cor = entity.getCor();
        this.dataFabricacao = entity.getDataFabricacao();
        this.perifericos = entity.getPerifericos();
    }

    public Computador(String nome, String cor, Long dataFabricacao) {
        this.nome = nome;
        this.cor = cor;
        this.dataFabricacao = dataFabricacao;
    }

    public void addPeriferico(Periferico periferico) {
        perifericos.add(periferico);
        periferico.setComputador(this);
    }

    public void removePeriferico(Periferico periferico) {
        perifericos.remove(periferico);
    }

}
