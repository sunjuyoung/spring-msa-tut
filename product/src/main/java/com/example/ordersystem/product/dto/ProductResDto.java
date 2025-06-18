package com.example.ordersystem.product.dto;

import com.example.ordersystem.product.entity.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductResDto {

    private Long productId;
    private String name;
    private int price;
    private int stockQuantity;
    private Long memberId;

    public static ProductResDto from(Product product) {
        return ProductResDto.builder()
                .productId(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .memberId(product.getMemberId())
                .build();
    }
}
