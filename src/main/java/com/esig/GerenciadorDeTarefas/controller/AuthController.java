package com.esig.GerenciadorDeTarefas.controller;

import com.esig.GerenciadorDeTarefas.security.dto.JwtResponse;
import com.esig.GerenciadorDeTarefas.security.dto.LoginRequest;
import com.esig.GerenciadorDeTarefas.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth") // Mapeamento base para /api/auth
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    /**
     * Endpoint para login.
     * URL: POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // 1. Tenta autenticar o usuário
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 2. Define a autenticação no contexto de segurança
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Gera o Token JWT usando o provedor
        String jwt = tokenProvider.createToken(authentication);

        // 4. Retorna o token na resposta
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    // NOTA: Para um sistema completo, você precisaria de um endpoint POST /api/auth/register
    // para criar novos usuários e salvar a senha com o BCrypt.
}