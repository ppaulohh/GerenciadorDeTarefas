package com.esig.GerenciadorDeTarefas.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds; // Em milissegundos

    private Key key;

    // Inicializa a chave de segurança usando a chave secreta (secretKey)
    private Key getSigningKey() {
        if (this.key == null) {
            this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        }
        return this.key;
    }

    /**
     * Cria o Token JWT para o usuário autenticado.
     */
    public String createToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        // Define a data de expiração
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // O assunto (username)
                .setIssuedAt(now) // Data de emissão
                .setExpiration(expiryDate) // Data de expiração
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // Assinatura com a chave secreta
                .compact();
    }

    /**
     * Obtém o nome de usuário do Token.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Valida a integridade e a validade do Token.
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            // Logs de erro podem ser úteis aqui (ex: token expirado, inválido, etc.)
            return false;
        }
    }
}
