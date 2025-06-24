package com.example.ordersystem.product.controller;

import com.example.ordersystem.product.entity.Product;
import com.example.ordersystem.product.dto.ProductRegisterDto;
import com.example.ordersystem.product.dto.ProductResDto;
import com.example.ordersystem.product.dto.ProductUpdateStockDto;
import com.example.ordersystem.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> productCreate(ProductRegisterDto dto, @RequestHeader("X-User-Id") String userId) {

        Product product = productService.productCreate(dto,userId);
        return new ResponseEntity<>(product.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResDto> productDetail(@PathVariable Long id) throws InterruptedException {
       // Thread.sleep(4000L);
       ProductResDto dto = productService.productDetail(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/updateStock")
    public ResponseEntity<?> productUpdateStock(@RequestBody ProductUpdateStockDto dto, @RequestHeader("X-User-Id") String userId) {
        Product product = productService.productUpdateStock(dto, userId);
        return new ResponseEntity<>(product.getId(), HttpStatus.OK);
    }


}
