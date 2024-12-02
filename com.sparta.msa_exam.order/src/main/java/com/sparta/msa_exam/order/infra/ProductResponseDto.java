package com.sparta.msa_exam.order.infra;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductResponseDto {
    private Long productId;
    private String productName;
    private Integer supplyPrice;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
