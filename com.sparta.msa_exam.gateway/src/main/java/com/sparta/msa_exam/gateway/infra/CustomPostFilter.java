package com.sparta.msa_exam.gateway.infra;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
public class CustomPostFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute("startTime");
            String traceId = exchange.getAttribute("traceId");

            if (startTime != null && traceId != null) {
                Long duration = System.currentTimeMillis() - startTime;

                ServerHttpResponse response = exchange.getResponse();
                Integer statusCode = response.getStatusCode() != null ? response.getStatusCode().value() :  - 1;
                log.info("[{}] Outgoing Response: StatusCode = {}, Duration = {}ms", traceId, statusCode, duration);

                URI routeUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
                if (routeUri != null && routeUri.getPort() != - 1) {
                    Integer port = routeUri.getPort();
                    response.getHeaders().add("Server-Port", String.valueOf(port));
                }
                log.info("[{}] Server-Port = {}", traceId, response.getHeaders().getFirst("Server-Port"));
            }

        }));
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}

