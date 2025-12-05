package com.esig.GerenciadorDeTarefas.repository;

import com.esig.GerenciadorDeTarefas.model.Tarefa;
import com.esig.GerenciadorDeTarefas.util.Situacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    public static final String FILTRO_TAREFAS_QUERY =
            "SELECT t FROM Tarefa t WHERE " +
                    // Filtro 1: ID
                    "(:id IS NULL OR t.id = :id) AND " +

                    // Filtro 2: Título OU Descrição
                    "(:termoBusca IS NULL OR " +
                    "    t.titulo LIKE CONCAT('%', :termoBusca, '%') OR " +
                    "    t.descricao LIKE CONCAT('%', :termoBusca, '%')" +
                    ") AND " +

                    // Filtro 3: Responsável
                    "(:responsavel IS NULL OR t.responsavel LIKE CONCAT('%', :responsavel, '%')) AND " +

                    // Filtro 4: Situação
                    "(:situacao IS NULL OR t.situacao = :situacao)";


    @Query(FILTRO_TAREFAS_QUERY)
    List<Tarefa> findByFilters(
            @Param("id") Long id,
            @Param("termoBusca") String termoBusca,
            @Param("responsavel") String responsavel,
            @Param("situacao") Situacao situacao
    );
}