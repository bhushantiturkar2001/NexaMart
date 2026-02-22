package com.nexamart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexamart.modal.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	List<Order> findByUserId(Long id);
	List<Order> findBySellerId(Long sellerId);

}
