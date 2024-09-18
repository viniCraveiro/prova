package br.edu.unicesumar.prova.restcontroller;

import static br.edu.unicesumar.prova.restcontroller.ComputadorValidator.validate;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.unicesumar.prova.domain.computador.Computador;
import br.edu.unicesumar.prova.domain.computador.ComputadorService;

@RestController
@RequestMapping("/api/computador")
public class ComputadorRestController {

    private final ComputadorService computadorService;

    public ComputadorRestController(ComputadorService computadorService) {
        this.computadorService = computadorService;
    }

    @PostMapping()
    public ResponseEntity<Computador> cadastrar(@RequestBody Computador computador) {
        validate(computador);
        Computador salvo = computadorService.salvarComputador(computador);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Computador> findById(@PathVariable UUID id) {
        Computador computador = computadorService.findById(id);
        return new ResponseEntity<>(computador, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Computador>> listAll() {
        List<Computador> computadores = computadorService.findAll();
        return new ResponseEntity<>(computadores, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Computador> editar(@RequestBody Computador editado) {
        Computador atualizado = computadorService.update(editado);
        return new ResponseEntity<>(new Computador(atualizado), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        computadorService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}
