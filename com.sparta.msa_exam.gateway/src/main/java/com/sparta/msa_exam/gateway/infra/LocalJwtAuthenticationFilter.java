package com.sparta.msa_exam.gateway.infra;

import com.sparta.msa_exam.gateway.exception.CustomException;
import com.sparta.msa_exam.gateway.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class LocalJwtAuthenticationFilter implements GlobalFilter {

    @Value("${service.jwt.secret-key}")
    private String secretKey;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        //todo:  path.equals("/auth/signUp/kakao") 삭제 (카카오 로그인 확인용)
        if (path.equals("/auth/signIn") || path.equals("/auth/signUp") || path.equals("/auth/signUp/kakao")) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);
        if (StringUtils.isEmpty(token)) {
            throw new CustomException(ErrorCode.AUTH_AUTHENTICATION_FAILED);
        }
        validateToken(token, exchange);
        return chain.filter(exchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private void validateToken(String token, ServerWebExchange exchange) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            exchange.getRequest().mutate()
                    .header("X-Username", claims.getSubject())
                    .build();
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
