package com.sparta.msa_exam.auth.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.msa_exam.auth.application.AuthService;
import com.sparta.msa_exam.auth.infra.KakaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final KakaoService kakaoService;
    private final AuthService authService;
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @GetMapping("/signUp/kakao")
    public ResponseEntity<Void> kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code);
        return ResponseEntity.ok().header(AUTHORIZATION_HEADER, token).build();
    }

    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserRequestDto requestDto) {
        authService.signUp(requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/signIn")
    public ResponseEntity<Void> signIn(@Valid @RequestBody UserRequestDto requestDto) {
        String token = authService.signIn(requestDto);
        return ResponseEntity.ok().header(AUTHORIZATION_HEADER, token).build();
    }

}
