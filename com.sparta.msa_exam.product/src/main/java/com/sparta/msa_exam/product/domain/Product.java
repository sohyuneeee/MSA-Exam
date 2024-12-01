package com.sparta.msa_exam.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer supplyPrice;

    @Column(nullable = false)
    private Long userId;

    @Builder
    private Product(String name, Integer supplyPrice, Long userId) {
        this.name = name;
        this.supplyPrice = supplyPrice;
        this.userId = userId;
    }

    public static Product of(String name, int supplyPrice, Long userId) {
        return Product.builder()
                .name(name)
                .supplyPrice(supplyPrice)
                .userId(userId)
                .build();
    }

}
