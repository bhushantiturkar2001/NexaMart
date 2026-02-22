package com.nexamart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexamart.domain.OrderStatus;
import com.nexamart.modal.Order;
import com.nexamart.modal.Seller;
import com.nexamart.service.OrderService;
import com.nexamart.service.SellerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

	private final OrderService orderService;
	private final SellerService sellerService;

	@GetMapping
	public ResponseEntity<List<Order>> getAllOrdersHandler(@RequestHeader("Authorization") String jwt)
			throws Exception {

		Seller seller = sellerService.getSellerProfile(jwt);
		List<Order> orders = orderService.sellersOrder(seller.getId());

		return new ResponseEntity<>(orders, HttpStatus.OK);

	}

	public ResponseEntity<Order> updateOrderHandler(@RequestHeader("Authorization") String jwt,
			@PathVariable Long orderId, @PathVariable OrderStatus orderStatus) throws Exception {

		Order updateOrderStatus = orderService.updateOrderStatus(orderId, orderStatus);

		return new ResponseEntity<>(updateOrderStatus, HttpStatus.ACCEPTED);

	}

}
