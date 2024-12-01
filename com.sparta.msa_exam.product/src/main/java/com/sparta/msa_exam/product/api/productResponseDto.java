package com.sparta.msa_exam.product.api;

import com.sparta.msa_exam.product.domain.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class productResponseDto {
    private Long productId;
    private String productName;
    private Integer supplyPrice;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private productResponseDto(Product product) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.supplyPrice = product.getSupplyPrice();
        this.userId = product.getUserId();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }

    public static productResponseDto from(Product product) {
        return productResponseDto.builder().product(product).build();
    }

}
