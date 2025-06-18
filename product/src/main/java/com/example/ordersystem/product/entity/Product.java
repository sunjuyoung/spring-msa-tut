package com.example.ordersystem.product.entity;

import com.example.ordersystem.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    public void minusStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("감소할 재고는 0보다 작을 수 없습니다.");
        }
        if (this.stockQuantity < stockQuantity) {
            throw new IllegalArgumentException("현재 재고보다 많은 수량을 감소시킬 수 없습니다.");
        }
        this.stockQuantity = this.stockQuantity - stockQuantity;
    }

    public void plusStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("증가할 재고는 0보다 작을 수 없습니다.");
        }
        this.stockQuantity = this.stockQuantity + stockQuantity;
    }
}
