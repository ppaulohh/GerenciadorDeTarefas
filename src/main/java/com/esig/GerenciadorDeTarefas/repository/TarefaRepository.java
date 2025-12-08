package com.esig.GerenciadorDeTarefas.repository;

import com.esig.GerenciadorDeTarefas.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    public static final String FILTRO_TAREFAS_NATIVE_QUERY =
            "SELECT * FROM tarefa t WHERE " +
                    "(:id IS NULL OR t.id = :id) AND " +

                    // Filtro 2: Título OU Descrição
                    "(:termoBusca IS NULL OR " +
                    "   LOWER(t.titulo) LIKE LOWER(CONCAT('%', :termoBusca, '%')) OR " +
                    "   LOWER(t.descricao) LIKE LOWER(CONCAT('%', :termoBusca, '%'))" +
                    ") AND " +

                    // Filtro 3: Responsável
                    "(:responsavel IS NULL OR LOWER(t.responsavel) LIKE LOWER(CONCAT('%', :responsavel, '%'))) AND " +

                    // Filtro 4: Situação
                    "(:situacao IS NULL OR t.situacao = :situacao)";

    @Query(value = FILTRO_TAREFAS_NATIVE_QUERY, nativeQuery = true)
    List<Tarefa> findByFiltersNative(
            @Param("id") Long id,
            @Param("termoBusca") String termoBusca,
            @Param("responsavel") String responsavel,
            @Param("situacao") String situacao // enum vira String no SQL
    );
}
