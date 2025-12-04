package com.esig.GerenciadorDeTarefas.repository;

import com.esig.GerenciadorDeTarefas.model.Tarefa;
import com.esig.GerenciadorDeTarefas.util.Situacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByTituloContainingOrDescricaoContainingAndSituacao(String titulo, String descricao, Situacao situacao);

    List<Tarefa> findBySituacao(Situacao situacao);
}