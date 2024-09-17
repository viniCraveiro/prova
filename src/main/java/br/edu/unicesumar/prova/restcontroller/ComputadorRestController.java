package br.edu.unicesumar.prova.restcontroller;

import br.edu.unicesumar.prova.domain.computador.Computador;
import br.edu.unicesumar.prova.domain.computador.ComputadorRepository;
import br.edu.unicesumar.prova.domain.periferico.Periferico;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static br.edu.unicesumar.prova.restcontroller.ComputadorValidator.validate;

@RestController
@RequestMapping("/api/computador")
public class ComputadorRestController {

    private final ComputadorRepository computadorRepository;

    public ComputadorRestController(ComputadorRepository computadorRepository) {
        this.computadorRepository = computadorRepository;
    }

    @PostMapping()
    public ResponseEntity<Computador> cadastrar(@RequestBody Computador entidade) {
        validate(entidade);
        Computador saved = computadorRepository.save(entidade);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Computador> findById(@PathVariable UUID id) {
        Computador computador = computadorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Computador não encontrado."));
        return new ResponseEntity<>(computador, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Computador>> listAll() {
        List<Computador> computadors = computadorRepository.findAll();
        return new ResponseEntity<>(computadors, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Computador> editar(@RequestBody Computador editado) {
        Computador computadorRecuperado = computadorRepository.findById(editado.getId()).orElseThrow(() -> new EntityNotFoundException("Computador não encontrado."));
        validate(editado);
        computadorRecuperado.setCor(editado.getCor());
        computadorRecuperado.setNome(editado.getNome());
        computadorRecuperado.setDataFabricacao(editado.getDataFabricacao());
        computadorRecuperado.getPerifericos().clear();
        for (Periferico periferico : editado.getPerifericos()) {
            computadorRecuperado.addPeriferico(periferico);
        }
        computadorRepository.save(computadorRecuperado);
        return new ResponseEntity<>(new Computador(computadorRecuperado), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        computadorRepository.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}
