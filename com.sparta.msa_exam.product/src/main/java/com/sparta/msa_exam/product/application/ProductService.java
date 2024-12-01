package com.sparta.msa_exam.product.application;

import com.sparta.msa_exam.product.api.ProductRequestDto;
import com.sparta.msa_exam.product.api.productResponseDto;
import com.sparta.msa_exam.product.domain.Product;
import com.sparta.msa_exam.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public productResponseDto createProduct(String userId, ProductRequestDto requestDto) {
        Product product = productRepository.save(Product.of(requestDto.getName(), requestDto.getSupplyPrice(), Long.parseLong(userId)));
        return productResponseDto.from(product);
    }

    public Page<productResponseDto> getProductList(int page, int size, String sortBy, boolean isAsc) {
        Pageable pageable = createPageableWithSorting(page, size, sortBy, isAsc);
        return productRepository.findAll(pageable).map(productResponseDto::from);
    }

    private Pageable createPageableWithSorting(int page, int size, String sortBy, boolean isAsc) {
        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        return PageRequest.of(page, size, sort);
    }
}
