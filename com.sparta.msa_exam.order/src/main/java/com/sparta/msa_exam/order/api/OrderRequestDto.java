package com.sparta.msa_exam.order.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto {

    @Valid
    @NotEmpty(message = "ORDER_ITEMS_EMPTY")
    private List<OrderItemRequestDto> orderItems;

}
