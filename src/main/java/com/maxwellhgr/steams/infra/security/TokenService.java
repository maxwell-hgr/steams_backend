package com.maxwellhgr.steams.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${token.secret}")
    private String secret;

    public String generateToken(String id) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("login-user-api")
                    .withSubject(id)
                    .withExpiresAt(this.generateExpirationTime())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException(e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("login-user-api")
                    .build()
                    .verify(token);
            System.out.println("Token valid: " + decodedJWT.getSubject());
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            System.out.println("Token verification failed: " + e.getMessage());
            return null;
        }
    }

    private Instant generateExpirationTime() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Missing or malformed Authorization header");
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }

    public String recoverIdFromRequest(HttpServletRequest request) {
        var token = recoverToken(request);
        if(token == null) return null;
        return validateToken(token);
    }
}
