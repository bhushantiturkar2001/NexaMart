package com.nexamart.service.impl;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.stereotype.Service;

import com.nexamart.modal.CartItem;
import com.nexamart.modal.User;
import com.nexamart.repository.CartItemRepository;
import com.nexamart.service.CartItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

	private final CartItemRepository cartItemRepository;

	@Override
	public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {

		CartItem item = findCartItemById(id);

		User cartItemUser = item.getCart().getUser();

		if (cartItemUser.getId().equals(userId)) {
			item.setQuantity(cartItem.getQuantity());
			item.setMrpPrice(item.getQuantity() * item.getProduct().getMrpPrice());
			item.setSellingPrice(item.getQuantity() * item.getProduct().getSellingPrice());

			return cartItemRepository.save(item);

		}
		throw new Exception("You cant update this cart item");
	}

	@Override
	public void removecartItem(Long userId, Long cartItemId) throws Exception {
		CartItem item = findCartItemById(cartItemId);

		User cartItemUser = item.getCart().getUser();

		if (cartItemUser.getId().equals(userId)) {
			cartItemRepository.delete(item);
		} else
			throw new Exception("You can't delete this item");

	}

	@Override
	public CartItem findCartItemById(Long id) throws Exception {

		return cartItemRepository.findById(id).orElseThrow(() -> new Exception("Cart item not found with id" + id));
	}

}
