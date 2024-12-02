package com.sparta.msa_exam.order.infra;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/products")
    Page<ProductResponseDto> getProductList();

    @GetMapping("/products/byIdList")
    List<ProductResponseDto> getProductsByIdList(@RequestParam List<Long> productIdList);

}
