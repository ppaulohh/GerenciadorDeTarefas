package com.esig.GerenciadorDeTarefas.config;

import com.esig.GerenciadorDeTarefas.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    // ======================================================================
    // PASSO 2: Exposição dos Beans (PasswordEncoder e AuthenticationManager)
    // ======================================================================

    /**
     * Define o codificador de senha (BCrypt) para criptografar as senhas no DB.
     * Necessário para o AuthenticationManager verificar a senha do login.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Expõe o AuthenticationManager que o Spring Security já configurou.
     * Ele é injetado e usado no AuthController para processar o login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ======================================================================
    // OUTRAS CONFIGURAÇÕES: Cadeia de Filtros
    // ======================================================================

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita CSRF e define sessão STATELESS para JWT
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 2. Configura as regras de autorização
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso SEM autenticação ao login e Swagger
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()

                        // Exige autenticação (TOKEN JWT) para todas as outras requisições
                        .anyRequest().authenticated()
                );

        // 3. Adiciona o filtro JWT customizado
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuração CORS (Cross-Origin Resource Sharing).
     * Essencial para permitir que o frontend acesse o backend.
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
