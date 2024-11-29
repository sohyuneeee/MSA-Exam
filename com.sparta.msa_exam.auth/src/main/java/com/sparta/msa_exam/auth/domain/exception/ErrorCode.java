package com.sparta.msa_exam.auth.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    COMMON_INVALID_PARAMETER("잘못된 파라미터입니다.", BAD_REQUEST),
    COMMON_SERVER_ERROR("서버에서 에러가 발생하였습니다.", INTERNAL_SERVER_ERROR),

    AUTH_AUTHENTICATION_FAILED("인증에 실패하셨습니다.", UNAUTHORIZED),
    AUTH_AUTHORIZATION_FAILED("권한이 없습니다.", FORBIDDEN),
    AUTH_JWT_CLAIMS_EMPTY("JWT claims 문자열이 비어 있습니다.", UNAUTHORIZED),
    AUTH_JWT_EXPIRED("만료된 토큰입니다.", UNAUTHORIZED),
    AUTH_JWT_INVALID("잘못된 토큰입니다.", UNAUTHORIZED),
    AUTH_JWT_UNSUPPORTED("지원되지 않는 토큰입니다.", UNAUTHORIZED),
    AUTH_JWT_UNPRIVILEGED("권한이 없는 토큰입니다.", FORBIDDEN),

    INVALID_PASSWORD("비밀번호가 잘못됐습니다.", FORBIDDEN),
    USER_NOT_FOUND("존재하지 않는 사용자입니다.", BAD_REQUEST),
    USERNAME_POLICY("이름은 영어소문자, 숫자로 이루어진 4자이상 10자 이하의 문자열이어야 합니다.", BAD_REQUEST),
    PASSWORD_POLICY("비밀번호는 영어대소문자, 숫자, 특수문자로 이루어진 8자 이상 15자 이하의 문자열이어야 합니다.", BAD_REQUEST),
    USER_DUPLICATED("중복된 이름입니다.", CONFLICT),
    USER_NOT_SAME("해당 사용자가 아닙니다.", BAD_REQUEST),

  ;

    private final String message;
    private final HttpStatus httpStatus;

}
