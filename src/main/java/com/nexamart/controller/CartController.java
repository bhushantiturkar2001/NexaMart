package com.nexamart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexamart.exception.ProductException;
import com.nexamart.modal.Cart;
import com.nexamart.modal.CartItem;
import com.nexamart.modal.Product;
import com.nexamart.modal.User;
import com.nexamart.request.AddItemRequest;
import com.nexamart.response.ApiResponse;
import com.nexamart.service.CartItemService;
import com.nexamart.service.CartService;
import com.nexamart.service.ProductService;
import com.nexamart.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
	
	private final CartItemService cartItemService;
	private final CartService cartService;
	private final UserService userService;
	private final ProductService productService;
	
	
	@GetMapping 
	public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findByJwtToken(jwt);
		
		Cart userCart = cartService.findUserCart(user);
		
		return new ResponseEntity<>(userCart,HttpStatus.OK);
	}
	
	@PutMapping("/add")
	public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest req, @RequestHeader("Authorization") String jwt) throws ProductException, Exception{
		User user = userService.findByJwtToken(jwt);
		
		Product product = productService.findProductById(req.getProductId());
		
		CartItem cartItem = cartService.addCartItem(user, product, req.getSize(), req.getQuantity());
		
		ApiResponse response = new ApiResponse();
		response.setMessage("Item added to cart sucessfully");
		
		return new ResponseEntity<CartItem>(cartItem,HttpStatus.ACCEPTED);
	
	}
	
	@DeleteMapping("/item/{cartItemId}")
	public ResponseEntity<ApiResponse> deleteCartItemHandler(@PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt ) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		
		cartItemService.removecartItem(user.getId(), cartItemId);
		
		ApiResponse res = new ApiResponse();
		res.setMessage("Item remove from cart");
		
		return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
		
	}
	
	@PutMapping("/item/{cartItemId}")
	public ResponseEntity<CartItem> updateCartItem(@RequestHeader("Authorization") String jwt,@PathVariable Long cartItemId, @RequestBody CartItem cartItem) throws Exception{
		
		User user = userService.findByJwtToken(jwt);
		
		CartItem updatedCartItem = null;
		if(cartItem.getQuantity()>0) {
		updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
		}
		
		return new ResponseEntity<>(updatedCartItem,HttpStatus.ACCEPTED);
		
	}
	
	

}
