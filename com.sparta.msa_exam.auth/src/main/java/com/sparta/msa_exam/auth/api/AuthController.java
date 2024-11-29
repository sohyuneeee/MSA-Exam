package com.sparta.msa_exam.auth.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.msa_exam.auth.infra.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final KakaoService kakaoService;

    @GetMapping("/signUp/kakao")
    public ResponseEntity<Void> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code);
        response.addHeader("Authorization", token);
        return ResponseEntity.ok().build();
    }

}
