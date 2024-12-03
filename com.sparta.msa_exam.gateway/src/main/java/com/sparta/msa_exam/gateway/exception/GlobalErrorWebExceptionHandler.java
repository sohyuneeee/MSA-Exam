package com.sparta.msa_exam.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    private static final String CODE_ATTR = "code";
    private static final String MESSAGE_ATTR = "message";
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        ErrorCode errorCode = getErrorCode(ex);

        Map<String, String> responseBodyMap = new HashMap<>();
        responseBodyMap.put(CODE_ATTR, errorCode.name());
        responseBodyMap.put(MESSAGE_ATTR, errorCode.getMessage());

        return writeErrorResponse(response, errorCode, responseBodyMap);
    }

    private Mono<Void> writeErrorResponse(ServerHttpResponse response, ErrorCode code, Map<String, String> responseBodyMap) {
        try {
            response.setStatusCode(code.getHttpStatus());
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            byte[] responseBody = objectMapper.writeValueAsBytes(responseBodyMap);
            DataBuffer buffer = response.bufferFactory().wrap(responseBody);
            return response.writeWith(Mono.just(buffer));

        } catch (JsonProcessingException e) {
            log.error("Error serializing error response", e);
            return Mono.error(e);
        }
    }

    private ErrorCode getErrorCode(Throwable ex) {
        if (ex instanceof CustomException) {
            return ((CustomException) ex).getErrorCode();
        } else {
            return ErrorCode.COMMON_SERVER_ERROR;
        }
    }

}
