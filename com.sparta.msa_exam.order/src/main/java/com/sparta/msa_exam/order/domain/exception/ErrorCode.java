package com.sparta.msa_exam.order.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    COMMON_INVALID_PARAMETER("잘못된 파라미터입니다.", BAD_REQUEST),
    COMMON_SERVER_ERROR("서버에서 에러가 발생하였습니다.", INTERNAL_SERVER_ERROR),

    USER_NOT_SAME("해당 작성자가 아닙니다.", BAD_REQUEST),

    PRODUCT_NOT_FOUND("상품이 존재하지 않습니다.", NOT_FOUND),
    PRODUCT_ID_EMPTY("상품 아이디가 존재하지 않습니다.", BAD_REQUEST),
    PRODUCT_SERVICE_UNAVAILABLE("상품 정보를 불러오는 데 실패했습니다. 잠시 후에 다시 시도해주세요.", BAD_GATEWAY),

    ORDER_NOT_FOUND("주문이 존재하지 않습니다.", NOT_FOUND),
    ORDER_ITEMS_EMPTY("주문 아이템이 존재하지 않습니다.", BAD_REQUEST),
    ORDER_ITEM_QUANTITY_EMPTY("주문 아이템 수량이 존재하지 않습니다.", BAD_REQUEST),
    ORDER_ITEM_QUANTITY_INVALID("주문 아이템 수량이 유효하지 않습니다.", BAD_REQUEST),
    ORDER_ITEM_PRICE_EMPTY("주문 아이템 가격이 존재하지 않습니다.", BAD_REQUEST),
    ORDER_ITEM_PRICE_INVALID("주문 아이템 가격이 유효하지 않습니다.", BAD_REQUEST),

  ;

    private final String message;
    private final HttpStatus httpStatus;

}
