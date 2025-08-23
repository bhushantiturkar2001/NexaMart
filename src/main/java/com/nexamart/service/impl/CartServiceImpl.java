package com.nexamart.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import com.nexamart.controller.UserController;
import com.nexamart.modal.Cart;
import com.nexamart.modal.CartItem;
import com.nexamart.modal.Product;
import com.nexamart.modal.User;
import com.nexamart.repository.CartItemRepository;
import com.nexamart.repository.CartRepository;
import com.nexamart.service.CartService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

    

	@Override
	public CartItem addCartItem(User user, Product product, String size, int quantity) {
		Cart cart = findUserCart(user);
		
		CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart, product, size);
		
		if (isPresent == null) {
			CartItem cartItem = new CartItem();
			
			cartItem.setProduct(product);
			cartItem.setProduct(product);
			cartItem.setUserId(user.getId());
			cartItem.setSize(size);
			
			int totalPrice = quantity* product.getSellingPrice();
			cartItem.setSellingPrice(totalPrice);
			cartItem.setMrpPrice(quantity*product.getMrpPrice());
			
			cart.getCartItem().add(cartItem);
			cartItem.setCart(cart);
			
			return cartItemRepository.save(cartItem);
		}
		return isPresent;
	}

	@Override
	public Cart findUserCart(User user) {
		
		Cart cart = cartRepository.findByUserId(user.getId());
		
		int totalPrice=0;
		int totalDiscountPrice=0;
		int totalItems=0;
		
		for(CartItem cartItem: cart.getCartItem()) {
			totalPrice+=cartItem.getMrpPrice();
			totalDiscountPrice+=cartItem.getSellingPrice();
			totalItems+=cartItem.getQuantity();
		}
		
		cart.setTotalMrpPrice(totalPrice);
		cart.setTotalItem(totalItems);
		cart.setTotalSellingPrice((double) totalDiscountPrice);
		cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountPrice));
		cart.setTotalItem(totalItems);
		
 		return cart;
	}
	
	private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
		if (mrpPrice<=0) {
			return 0;
					
		}
		
		double discount = mrpPrice - sellingPrice;
		double discountPercentage =(discount/mrpPrice)*100;
		
		return (int)discountPercentage;
	}

}
