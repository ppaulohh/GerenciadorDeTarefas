package com.esig.GerenciadorDeTarefas.service;

import com.esig.GerenciadorDeTarefas.model.Tarefa;
import com.esig.GerenciadorDeTarefas.repository.TarefaRepository;
import com.esig.GerenciadorDeTarefas.util.Situacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;


    public Tarefa criarTarefa(Tarefa tarefa) {
        tarefa.setSituacao(Situacao.EM_ANDAMENTO);
        return tarefaRepository.save(tarefa);
    }


    public Tarefa atualizarTarefa(Long id, Tarefa tarefaAtualizada) {
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));


        if (tarefaAtualizada.getTitulo() != null) {
            tarefaExistente.setTitulo(tarefaAtualizada.getTitulo());
        }
        if (tarefaAtualizada.getDescricao() != null) {
            tarefaExistente.setDescricao(tarefaAtualizada.getDescricao());
        }
        if (tarefaAtualizada.getResponsavel() != null) {
            tarefaExistente.setResponsavel(tarefaAtualizada.getResponsavel());
        }
        if (tarefaAtualizada.getPrioridade() != null) {
            tarefaExistente.setPrioridade(tarefaAtualizada.getPrioridade());
        }
        if (tarefaAtualizada.getDeadline() != null) {
            tarefaExistente.setDeadline(tarefaAtualizada.getDeadline());
        }
        if (tarefaAtualizada.getSituacao() != null) {
            tarefaExistente.setSituacao(tarefaAtualizada.getSituacao());
        }

        return tarefaRepository.save(tarefaExistente);
    }

    public void removerTarefa(Long id) {
        tarefaRepository.deleteById(id);
    }

    public Tarefa concluirTarefa(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        tarefa.setSituacao(Situacao.CONCLUIDA); // Marca como concluída [cite: 46]
        return tarefaRepository.save(tarefa);
    }

    public List<Tarefa> listarTarefasComFiltros(
            Long id,
            String termoBusca,
            String responsavel,
            String concluida
    ) {
        return tarefaRepository.findByFiltersNative(
                id,
                termoBusca,
                responsavel,
                concluida
        );
    }
}
