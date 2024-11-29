package com.sparta.msa_exam.auth.util;

import com.sparta.msa_exam.auth.domain.exception.CustomException;
import com.sparta.msa_exam.auth.domain.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_EXPIRATION = 60 * 60 * 1000L;

    @Value("${service.jwt.secret-key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(String username) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .setExpiration(new Date(date.getTime() + TOKEN_EXPIRATION))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public Claims getUserInfoFromToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (SecurityException | MalformedJwtException e) {
            throw new CustomException(ErrorCode.AUTH_JWT_INVALID);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.AUTH_JWT_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(ErrorCode.AUTH_JWT_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.AUTH_JWT_CLAIMS_EMPTY);
        }
    }

}
