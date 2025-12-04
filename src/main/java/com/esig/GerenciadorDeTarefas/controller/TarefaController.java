package com.esig.GerenciadorDeTarefas.controller;

import com.esig.GerenciadorDeTarefas.model.Tarefa;
import com.esig.GerenciadorDeTarefas.service.TarefaService;
import com.esig.GerenciadorDeTarefas.util.Situacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@RequestBody Tarefa tarefa) {
        // *Este endpoint deve exigir autenticação JWT *
        Tarefa novaTarefa = tarefaService.criarTarefa(tarefa);
        return new ResponseEntity<>(novaTarefa, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTarefas(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false, defaultValue = "EM_ANDAMENTO") Situacao situacao) {
        // *Este endpoint deve exigir autenticação JWT *
        List<Tarefa> tarefas = tarefaService.listarTarefas(termo, situacao);
        return ResponseEntity.ok(tarefas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarTarefa(@PathVariable Long id, @RequestBody Tarefa tarefa) {
        // *Este endpoint deve exigir autenticação JWT *
        Tarefa tarefaAtualizada = tarefaService.atualizarTarefa(id, tarefa);
        return ResponseEntity.ok(tarefaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerTarefa(@PathVariable Long id) {
        // *Este endpoint deve exigir autenticação JWT *
        tarefaService.removerTarefa(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/tarefas/{id}/concluir (Concluir Tarefa) [cite: 46]
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Tarefa> concluirTarefa(@PathVariable Long id) {
        // *Este endpoint deve exigir autenticação JWT *
        Tarefa tarefaConcluida = tarefaService.concluirTarefa(id);
        return ResponseEntity.ok(tarefaConcluida);
    }
}
