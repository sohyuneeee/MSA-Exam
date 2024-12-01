package com.sparta.msa_exam.product.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class ProductRequestDto {

    @NotBlank(message = "PRODUCT_NAME_EMPTY")
    private String name;

    @NotNull(message = "PRODUCT_PRICE_EMPTY")
    @Positive(message = "PRODUCT_PRICE_INVALID")
    private Integer supplyPrice;
}
