package com.example.ordersystem.ordering.repository;

import com.example.ordersystem.ordering.entity.Ordering;
import com.example.ordersystem.ordering.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderingRepository extends JpaRepository<Ordering, Long> {
  Optional<Ordering> findByProductIdAndOrderStatus(Long productId, OrderStatus orderStatus);
}
