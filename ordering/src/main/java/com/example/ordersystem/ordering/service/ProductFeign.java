package com.example.ordersystem.ordering.service;

import com.example.ordersystem.ordering.dto.ProductDto;
import com.example.ordersystem.ordering.dto.ProductUpdateStockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//name 은 eureka에 등록된 서비스 이름
@FeignClient(name = "product-service")
public interface ProductFeign {

    @GetMapping("/product/{productId}")
    ProductDto getProductById(@PathVariable Long productId);

    @PutMapping("/product/updateStock")
    void updateStock(@RequestBody ProductUpdateStockDto productUpdateStockDto,@RequestHeader("X-User-Id") String userId);
}
