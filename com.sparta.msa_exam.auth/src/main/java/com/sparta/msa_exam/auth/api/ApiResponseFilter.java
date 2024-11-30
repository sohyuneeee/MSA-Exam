package com.sparta.msa_exam.auth.api;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class ApiResponseFilter extends OncePerRequestFilter {

    @Value("${server.port}")
    private String serverPort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.addHeader("Server-Port", serverPort);
        log.info("Server port: {}", response.getHeader("Server-Port"));
        filterChain.doFilter(request, response);
    }

}
