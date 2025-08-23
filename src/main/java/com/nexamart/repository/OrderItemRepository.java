package com.nexamart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexamart.modal.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
