package com.example.ordersystem.ordering.dto;


import lombok.Data;

@Data
public class ProductUpdateStockDto {

    private Long productId;
    private Integer stockQuantity;



    public ProductUpdateStockDto(Long productId, Integer stockQuantity) {
        this.productId = productId;
        this.stockQuantity = stockQuantity;
    }

}
