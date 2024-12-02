package com.sparta.msa_exam.product.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    COMMON_INVALID_PARAMETER("잘못된 파라미터입니다.", BAD_REQUEST),
    COMMON_SERVER_ERROR("서버에서 에러가 발생하였습니다.", INTERNAL_SERVER_ERROR),

    PRODUCT_NOT_FOUND("상품이 존재하지 않습니다.", NOT_FOUND),
    PRODUCT_NAME_EMPTY("상품명이 존재하지 않습니다.", BAD_REQUEST),
    PRODUCT_PRICE_EMPTY("상품 가격이 존재하지 않습니다.", BAD_REQUEST),
    PRODUCT_PRICE_INVALID("상품가격이 유효하지 않습니다.", BAD_REQUEST),

  ;

    private final String message;
    private final HttpStatus httpStatus;

}
