package com.esig.GerenciadorDeTarefas.security.jwt;

import com.esig.GerenciadorDeTarefas.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Prefixo padrão de tokens JWT no cabeçalho Authorization
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. Tenta extrair o JWT da requisição
            String jwt = getJwtFromRequest(request);

            if (jwt != null && tokenProvider.validateToken(jwt)) {
                // 2. Se o token é válido, extrai o username
                String username = tokenProvider.getUsernameFromToken(jwt);

                // 3. Carrega os detalhes do usuário pelo username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 4. Cria o objeto de autenticação para o Spring Security
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // 5. Adiciona detalhes da requisição (útil para logs e auditoria)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Define o usuário como autenticado no contexto de segurança do Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // Em caso de falha (token expirado, inválido, etc.), a requisição será bloqueada
            logger.error("Não foi possível setar a autenticação do usuário no contexto de segurança: " + ex.getMessage());
        }

        // Continua o encadeamento de filtros (permite que a requisição siga para o Controller)
        filterChain.doFilter(request, response);
    }

    /**
     * Método auxiliar para extrair o JWT do cabeçalho de Autorização (Authorization: Bearer <token>).
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Verifica se o cabeçalho existe e começa com o prefixo "Bearer "
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            // Retorna apenas a string do token
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
