package com.esig.GerenciadorDeTarefas.repository;

import com.esig.GerenciadorDeTarefas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método usado pelo UserDetailsService
    Optional<Usuario> findByUsername(String username);
}
