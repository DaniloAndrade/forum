package br.com.alura.forum.security.jwt;

import br.com.alura.forum.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Component
public class TokenManager {

    @Value("${alura.forum.jwt.secret:123456}")
    private String secret;

    @Value("${alura.forum.jwt.expiration:604800000}")
    private Long expirationInMillis;


    public String generateToken(Authentication authenticate) {
        User user = (User) authenticate.getPrincipal();

        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + expirationInMillis);

        return Jwts.builder()
                .setIssuer("Alura Forum API")
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secret).compact();

    }

    public <T> Optional<T> getIfValid(String jwt, Function<Jwt, T> transform) {
        try {
            return Optional.ofNullable(Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt)).map(transform);
        }catch (RuntimeException ex) {
            return Optional.empty();
        }
    }
}
