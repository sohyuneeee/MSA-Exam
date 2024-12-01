package com.sparta.msa_exam.gateway.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@Slf4j
@Component
public class CustomPostFilter implements GlobalFilter, Ordered {

    private static final String START_TIME_ATTR = "startTime";
    private static final String TRACE_ID_ATTR = "traceId";
    private static final String GATEWAY_REQUEST_URL_ATTR = ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
    private static final String SERVER_PORT_HEADER = "Server-Port";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> handlePostFilter(exchange)));
    }

    private void handlePostFilter(ServerWebExchange exchange) {
        String traceId = exchange.getAttribute(TRACE_ID_ATTR);
        if(traceId == null) {
            traceId = UUID.randomUUID().toString();
        }
        logResponse(exchange, traceId);
        addServerPortHeader(exchange, traceId);
    }

    private void logResponse(ServerWebExchange exchange, String traceId) {
        Long startTime = exchange.getAttribute(START_TIME_ATTR);
        if(startTime == null) {
            startTime = System.currentTimeMillis();
        }

        Long duration = System.currentTimeMillis() - startTime;
        ServerHttpResponse response = exchange.getResponse();
        Integer statusCode = response.getStatusCode() != null ? response.getStatusCode().value() :  - 1;
        log.info("[{}] Outgoing Response: StatusCode = {}, Duration = {}ms", traceId, statusCode, duration);
    }

    private void addServerPortHeader(ServerWebExchange exchange, String traceId) {
        URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        if (routeUri != null && routeUri.getPort() != - 1) {
            Integer port = routeUri.getPort();
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().add(SERVER_PORT_HEADER, String.valueOf(port));
            log.info("[{}] Server-Port = {}", traceId, response.getHeaders().getFirst(SERVER_PORT_HEADER));
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

}
