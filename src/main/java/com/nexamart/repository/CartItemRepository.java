package com.nexamart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexamart.modal.Cart;
import com.nexamart.modal.CartItem;
import com.nexamart.modal.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
      CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
