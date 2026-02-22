package com.nexamart.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexamart.modal.Product;
import com.nexamart.modal.User;
import com.nexamart.modal.WishList;
import com.nexamart.service.ProductService;
import com.nexamart.service.UserService;
import com.nexamart.service.WishListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishListController {

	private final WishListService wishListService;
	private final UserService userService;
	private final ProductService productService;

	@GetMapping()
	public ResponseEntity<WishList> getWishListByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findByJwtToken(jwt);

		WishList wishList = wishListService.getWishListByUserId(user);

		return ResponseEntity.ok(wishList);
	}

	@PostMapping("/add-product/{productId}")
	public ResponseEntity<WishList> addProductToWishList(@PathVariable Long productId,
			@RequestHeader("Authorization") String jwt) throws Exception {

		Product product = productService.findProductById(productId);
		User user = userService.findByJwtToken(jwt);
		WishList updatedWishList = wishListService.addProductToWishList(user, product);

		return ResponseEntity.ok(updatedWishList);
	}

}
