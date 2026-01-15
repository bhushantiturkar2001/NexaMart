package com.nexamart.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexamart.modal.Cart;
import com.nexamart.modal.Coupen;
import com.nexamart.modal.User;
import com.nexamart.repository.CoupenRepository;
import com.nexamart.service.CartService;
import com.nexamart.service.CoupenService;
import com.nexamart.service.UserService;

import lombok.RequiredArgsConstructor;
// admin controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupens")
public class AdminCoupenController {

	private final CoupenRepository coupenRepository;

	private final CoupenService coupenService;
	private final UserService userService;
	private final CartService cartService;

	

	@PostMapping("/apply")
	public ResponseEntity<Cart> applyCoupen(@RequestParam String apply, @RequestParam String code,
			@RequestParam double orderValue, @RequestHeader("Authorization") String jwt) throws Exception {

		User user = userService.findByJwtToken(jwt);
		Cart cart;

		if (apply.equals("true")) {
			cart = coupenService.applyCoupen(code, orderValue, user);
		} else {
			cart = coupenService.removedCoupen(code, user);
		}

		return ResponseEntity.ok(cart);

	}

	// ADMIN controller
	@PostMapping("/admin/create")
	public ResponseEntity<Coupen> createCoupen(@RequestBody Coupen coupen) {

		Coupen createdCoupen = coupenService.createCoupen(coupen);
		return ResponseEntity.ok(createdCoupen);

	}

	@DeleteMapping("/admin/delete/{id}")
	public ResponseEntity<?> deleteCoupen(@PathVariable Long id) throws Exception {

		coupenService.deleteCoupen(id);
		return ResponseEntity.ok("Copen deleted successfully");

	}

	@GetMapping("/admin/all")
	public ResponseEntity<List<Coupen>> getAllCoupen() {

		List<Coupen> allCoupen = coupenService.findAllCoupen();
		return ResponseEntity.ok(allCoupen);

	}

}
