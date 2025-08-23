package com.nexamart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexamart.modal.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	
	Cart findByUserId(Long id);

}
