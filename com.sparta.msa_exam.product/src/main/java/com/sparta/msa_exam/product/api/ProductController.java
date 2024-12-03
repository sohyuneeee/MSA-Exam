package com.sparta.msa_exam.product.api;

import com.sparta.msa_exam.product.application.ProductService;
import com.sparta.msa_exam.product.config.RestPage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<ProductResponseDto> createProduct(@RequestHeader(value = "X-User-Id") String userId,
                                                            @Valid @RequestBody ProductRequestDto requestDto) {
        ProductResponseDto responseDto = productService.createProduct(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping()
    public ResponseEntity<RestPage<ProductResponseDto>> getProductList(@RequestParam(defaultValue = "1") int page,
                                                                       @RequestParam(defaultValue = "10") int size,
                                                                       @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                       @RequestParam(defaultValue = "false") boolean isAsc) {
        RestPage<ProductResponseDto> responseDtoPage = productService.getProductList(page - 1, size, sortBy, isAsc);
        return ResponseEntity.ok(responseDtoPage);
    }

    @GetMapping("/byIdList")
    public ResponseEntity<List<ProductResponseDto>> getProductsByIdList(@RequestParam List<Long> productIdList) {
        List<ProductResponseDto> productList = productService.getProductsByIdList(productIdList);
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDto>> getAllProduct() {
        List<ProductResponseDto> responseDtoPage = productService.getAllProduct();
        return ResponseEntity.ok(responseDtoPage);
    }

}
