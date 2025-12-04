package com.esig.GerenciadorDeTarefas.model;

import com.esig.GerenciadorDeTarefas.util.Prioridade;
import com.esig.GerenciadorDeTarefas.util.Situacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private String responsavel;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private Situacao situacao;

}