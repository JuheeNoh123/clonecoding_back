package CGVcloneCoding.cloneCoding.service;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTUtility {
    private String secret = "yoursecretkey";

    private static final long EXPIRATION_TIME = 1000L * 60 * 60;

    public String generateJwtToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();

            return claims;
        } catch (ExpiredJwtException ex) {
            // 토큰이 만료된 경우
            throw new RuntimeException("토큰이 만료되었습니다: " + ex.getMessage());
        } catch (JwtException | IllegalArgumentException ex) {
            // 토큰이 유효하지 않거나 잘못된 경우
            throw new RuntimeException("잘못된 토큰입니다: " + ex.getMessage());
        }
    }
}
