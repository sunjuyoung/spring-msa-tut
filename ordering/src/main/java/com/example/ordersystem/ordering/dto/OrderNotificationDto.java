package com.example.ordersystem.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderNotificationDto {
  private Long memberId;
  private Long orderId;
  private String message;
  private String notificationType; // ORDER_CANCELLED
}