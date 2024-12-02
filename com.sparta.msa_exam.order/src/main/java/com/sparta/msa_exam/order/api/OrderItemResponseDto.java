package com.sparta.msa_exam.order.api;

import com.sparta.msa_exam.order.domain.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderItemResponseDto {
    private final Long productId;
    private final Integer quantity;
    private final Integer unitPrice;

    @Builder
    private OrderItemResponseDto(Long productId, Integer quantity, Integer unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public static OrderItemResponseDto from(OrderItem orderItem) {
        return OrderItemResponseDto.builder()
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .build();
    }

}
