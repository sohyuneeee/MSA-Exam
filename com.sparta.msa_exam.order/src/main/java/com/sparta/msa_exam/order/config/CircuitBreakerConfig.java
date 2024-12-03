package com.sparta.msa_exam.order.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CircuitBreakerConfig {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @PostConstruct
    public void registerCircuitBreakerListeners() {
        circuitBreakerRegistry.circuitBreaker("order-service").getEventPublisher()
                .onStateTransition(event -> log.info("#### CircuitBreaker State Transition: {}", event))
                .onFailureRateExceeded(event -> log.info("#### CircuitBreaker Failure Rate Exceeded: {}", event))
                .onCallNotPermitted(event -> log.info("#### CircuitBreaker Call Not Permitted: {}", event))
                .onError(event -> log.info("#### CircuitBreaker Error: {}", event));
    }

}
