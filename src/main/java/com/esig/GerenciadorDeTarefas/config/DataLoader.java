package com.esig.GerenciadorDeTarefas.config;

import com.esig.GerenciadorDeTarefas.model.Usuario;
import com.esig.GerenciadorDeTarefas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Verifica se o usuário de teste já existe para evitar duplicatas
        if (usuarioRepository.findByUsername("teste@esig.com").isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setUsername("teste@esig.com");

            // 2. Criptografa a senha ANTES de salvar no banco
            usuario.setPassword(passwordEncoder.encode("senha123"));

            usuarioRepository.save(usuario);
        }
    }
}
