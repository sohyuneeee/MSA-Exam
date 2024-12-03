package com.sparta.msa_exam.order.api;

import com.sparta.msa_exam.order.domain.Order;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private Long userId;
    private List<OrderItemResponseDto> orderItemList = new ArrayList<>();


    @Builder
    private OrderResponseDto(Long orderId, Long userId, List<OrderItemResponseDto> orderItemList) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderItemList = orderItemList;

    }

    public static OrderResponseDto from(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .orderItemList(order.getOrderItemList().stream().map(OrderItemResponseDto::from).collect(Collectors.toList()))
                .build();
    }

}
