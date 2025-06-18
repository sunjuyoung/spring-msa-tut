package com.example.ordersystem.product.service;

import com.example.ordersystem.product.entity.Product;
import com.example.ordersystem.product.dto.ProductRegisterDto;
import com.example.ordersystem.product.dto.ProductResDto;
import com.example.ordersystem.product.dto.ProductUpdateStockDto;
import com.example.ordersystem.product.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public Product productCreate(ProductRegisterDto dto, String userId) {
        Product product = productRepository.save(dto.toEntity(Long.parseLong(userId)));
        return product;
    }

    public ProductResDto productDetail(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));
        return ProductResDto.from(product);
    }

    public Product productUpdateStock(ProductUpdateStockDto dto, String userId) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));
        if (!product.getMemberId().equals(Long.parseLong(userId))) {
            throw new IllegalArgumentException("상품의 소유자가 아닙니다.");
        }
        product.minusStockQuantity(dto.getStockQuantity());
        return product;
    }

    public Product productUpdateStock(ProductUpdateStockDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));
        product.minusStockQuantity(dto.getStockQuantity());
        return product;
    }

    @KafkaListener(topics = "product-update-stock-topic", containerFactory = "kafkaListener", groupId = "product-service")
    public void updateStockConsumer(String message) {
        ProductUpdateStockDto dto = null;
        try {
            dto = objectMapper.readValue(message, ProductUpdateStockDto.class);
            System.out.printf("ProductService.updateStockConsumer: %s\n", dto);
            productUpdateStock(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process stock update message", e);
        } catch (Exception e) {
            // 재고 감소 실패 시 실패 메시지를 ordering 서비스로 전송
            if (dto != null) {
                try {
                    System.out.println("재고 부족 카프카 전송");
                    // 실패한 재고 업데이트 정보를 그대로 전달
                    String failureMessage = objectMapper.writeValueAsString(dto);
                    kafkaTemplate.send("product-update-stock-failed-topic", failureMessage);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException("Failed to process failure message", ex);
                }
            }
            throw new RuntimeException("Failed to update stock", e);
        }
    }

}
