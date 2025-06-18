package com.example.ordersystem.ordering.service;

import com.example.ordersystem.ordering.entity.Ordering;
import com.example.ordersystem.ordering.entity.OrderStatus;
import com.example.ordersystem.ordering.dto.OrderCreateDto;
import com.example.ordersystem.ordering.dto.ProductDto;
import com.example.ordersystem.ordering.dto.ProductUpdateStockDto;
import com.example.ordersystem.ordering.repository.OrderingRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderingService {

    private final OrderingRepository orderingRepository;
    // private final RestTemplate restTemplate;
    private final ProductFeign productFeign;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    //circuit breaker는 해당 name을 가진 해당 메서드에 한해서만 유효하다.
    //open되어도 다른 메서드에서 product service에 요청을 보내는것은 가능하다.
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProductService")
    public Ordering orderFeignKafka(OrderCreateDto orderDto, String userId) {

        // 상품 정보 조회
        ProductDto productDto = productFeign.getProductById(orderDto.getProductId());

        int quantity = orderDto.getProductCount();
        if (productDto.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("재고 부족");
        }

        // 주문 생성 시 PENDING 상태로 설정
        Ordering ordering = Ordering.builder()
                .memberId(Long.parseLong(userId))
                .productId(orderDto.getProductId())
                .quantity(orderDto.getProductCount())
                .orderStatus(OrderStatus.PENDING)
                .build();
        orderingRepository.save(ordering);

        // 재고 감소 이벤트 발행
        ProductUpdateStockDto productUpdateStockDto = new ProductUpdateStockDto(orderDto.getProductId(),
                orderDto.getProductCount());
        kafkaTemplate.send("product-update-stock-topic", productUpdateStockDto);

        return ordering;
    }

    //메서드 매개변수 , 반환값은 같아야 한다.
    public Ordering fallbackProductService(OrderCreateDto orderDto, String userId, Throwable t) {
        throw new RuntimeException("Product Service 응답이 없습니다. 잠시 후 다시 시도해주세요.");
        //t.getMessage()를 통해서 예외 메시지를 확인할 수 있다.
    }

    // 재고 감소 실패 시 보상 트랜잭션 처리
    @KafkaListener(topics = "product-update-stock-failed-topic")
    public void handleStockUpdateFailure(ProductUpdateStockDto failedUpdate) {
        Ordering ordering = orderingRepository.findByProductIdAndOrderStatus(
                failedUpdate.getProductId(),
                OrderStatus.PENDING).orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        // 주문 상태를 CANCELLED로 변경
        ordering.setStatus(OrderStatus.CANCELLED);
        orderingRepository.save(ordering);

        // 주문 취소 알림 발행
//        OrderNotificationDto notification = OrderNotificationDto.builder()
//                .memberId(ordering.getMemberId())
//                .orderId(ordering.getId())
//                .message("주문이 취소되었습니다. 재고 부족으로 인해 주문을 처리할 수 없습니다.")
//                .notificationType("ORDER_CANCELLED")
//                .build();
//
//        kafkaTemplate.send("order-fail-notification-topic", notification);
    }

}
