package br.edu.unicesumar.prova.domain.computador;

import br.edu.unicesumar.prova.domain.periferico.Periferico;
import br.edu.unicesumar.prova.exception.ComputadorNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ComputadorService {

    private final ComputadorRepository computadorRepository;

    public ComputadorService(ComputadorRepository computadorRepository) {
        this.computadorRepository = computadorRepository;
    }

    public Computador salvarComputador(Computador computador) {
        for (Periferico periferico : computador.getPerifericos()) {
            periferico.setComputador(computador);
        }
        return computadorRepository.save(computador);
    }

    public Computador findById(UUID uuid) {
        return computadorRepository.findById(uuid).orElseThrow(() -> new ComputadorNaoEncontradoException("Computador não encontrado."));
    }

    public void deleteById(UUID uuid) {
        computadorRepository.deleteById(uuid);
    }

    public Computador update(Computador computador) {
        Computador computadorRecuperado = computadorRepository.findById(computador.getId()).orElseThrow(() -> new ComputadorNaoEncontradoException(
                "Computador não encontrado."));
        computadorRecuperado.setCor(computador.getCor());
        computadorRecuperado.setNome(computador.getNome());
        computadorRecuperado.setDataFabricacao(computador.getDataFabricacao());
        computadorRecuperado.getPerifericos().clear();
        for (Periferico periferico : computador.getPerifericos()) {
            computadorRecuperado.addPeriferico(periferico);
        }
        return computadorRepository.save(computadorRecuperado);
    }

    public List<Computador> findAll() {
        return computadorRepository.findAll();
    }

}
