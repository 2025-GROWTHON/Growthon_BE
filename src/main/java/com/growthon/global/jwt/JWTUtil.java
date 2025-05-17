package com.growthon.global.jwt;

import com.growthon.global.error.ErrorCode;
import com.growthon.global.jwt.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    // username 추출
    public String getUsername(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("username", String.class);
        } catch (JwtException e) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
    }

    // role 추출
    public String getRole(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("role", String.class);
        } catch (JwtException e) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }
    }

    // 만료 여부 확인
    public boolean isExpired(String token) {
        try {
            Date expiration = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException(ErrorCode.TOKEN_EXPIRED); // 만료된 토큰
        } catch (JwtException e) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN); // 기타 유효하지 않은 토큰
        }
    }

    // JWT 생성
    public String createJwt(String username, String role, Long expiredMs) {
        return Jwts.builder()
                .subject(username)  // subject 필수 아님, but 권장
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

}