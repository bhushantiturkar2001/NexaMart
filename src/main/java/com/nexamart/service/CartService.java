package com.nexamart.service;

import com.nexamart.modal.Cart;
import com.nexamart.modal.CartItem;
import com.nexamart.modal.Product;
import com.nexamart.modal.User;

public interface CartService {

	public CartItem addCartItem(User user, Product product, String size, int quantity);
	public Cart findUserCart(User user);

}
