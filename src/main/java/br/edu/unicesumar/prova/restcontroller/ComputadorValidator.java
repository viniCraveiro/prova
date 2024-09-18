package br.edu.unicesumar.prova.restcontroller;

import br.edu.unicesumar.prova.domain.computador.Computador;

public abstract class ComputadorValidator {

    public static void validate(Computador entidade){
        if(entidade.getNome() == null || entidade.getNome().isEmpty()) throw new RuntimeException("Informe um nome.");
        if(entidade.getCor() == null || entidade.getCor().isEmpty()) throw new RuntimeException("Informe uma cor.");
        if(entidade.getDataFabricacao() == null) throw new RuntimeException("Informe um ano de fabricação.");
        if(entidade.getPerifericos().isEmpty()) throw new RuntimeException("Informe ao menos um periferico.");
    }
}
