package com.sparta.msa_exam.order.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class OrderItemRequestDto {
    @NotNull(message = "PRODUCT_ID_EMPTY")
    private Long productId;

    @NotNull(message = "ORDER_ITEM_QUANTITY_EMPTY")
    @Positive(message = "ORDER_ITEM_QUANTITY_INVALID")
    private Integer quantity;

    @NotNull(message = "ORDER_ITEM_PRICE_EMPTY")
    @Positive(message = "ORDER_ITEM_PRICE_INVALID")
    private Integer unitPrice;
}
