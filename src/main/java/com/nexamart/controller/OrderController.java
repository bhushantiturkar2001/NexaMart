package com.nexamart.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexamart.domain.PaymentMethod;
import com.nexamart.modal.Address;
import com.nexamart.modal.Cart;
import com.nexamart.modal.Order;
import com.nexamart.modal.OrderItem;
import com.nexamart.modal.PaymentOrder;
import com.nexamart.modal.Seller;
import com.nexamart.modal.SellerReport;
import com.nexamart.modal.User;
import com.nexamart.repository.PaymentOrderRepository;
import com.nexamart.response.PaymentLinkResponse;
import com.nexamart.service.CartService;
import com.nexamart.service.OrderService;
import com.nexamart.service.PaymentService;
import com.nexamart.service.SellerReportService;
import com.nexamart.service.SellerService;
import com.nexamart.service.UserService;
import com.razorpay.PaymentLink;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

	private final SecurityFilterChain securityFilterChain;

	private final OrderService orderService;
	private final UserService userService;
	private final CartService cartService;
	private final SellerService sellerService;
	private final SellerReportService sellerReportService;
	private final PaymentService paymentService;
	private final PaymentOrderRepository paymentOrderRepository;

	@PostMapping()
	public ResponseEntity<PaymentLinkResponse> createOrderHandler(@RequestBody Address shippingAddress,
			@RequestParam PaymentMethod paymentMethod, @RequestHeader("Authorization") String jwt) throws Exception {

		User user = userService.findByJwtToken(jwt);
		Cart cart = cartService.findUserCart(user);

		Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);

		PaymentOrder paymentOrder = paymentService.createOrder(user, orders);

		PaymentLinkResponse res = new PaymentLinkResponse();

		if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {

			PaymentLink payment = paymentService.createRazorpayPaymentLink(user, paymentOrder.getAmount(),
					paymentOrder.getId());

			String paymentUrl = payment.get("short_url");
			String paymentUrlId = payment.get("id");

			res.setPayment_link_id(paymentUrl);

			paymentOrder.setPaymentLinkId(paymentUrlId);
			paymentOrderRepository.save(paymentOrder);

		}else {
			String paymentUrl = paymentService.createStripePaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
			
			res.setPayment_link_url(paymentUrl);
		}

		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)
			throws Exception {
		User user = userService.findByJwtToken(jwt);
		Order orders = orderService.findOrderById(orderId);

		return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);

	}

	@GetMapping("/item/{orderItemId}")
	public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long orderItemId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findByJwtToken(jwt);
		OrderItem orderItem = orderService.getOrderItemById(orderItemId);

		return new ResponseEntity<>(orderItem, HttpStatus.ACCEPTED);

	}

	public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)
			throws Exception {
		User user = userService.findByJwtToken(jwt);
		Order order = orderService.cancelOrder(orderId, user);

		Seller seller = sellerService.getSellerById(order.getSellerId());
		SellerReport report = sellerReportService.getSellerReport(seller);

		report.setCancelOrder(report.getCancelOrder() + 1);
		report.setTotalRefunds(report.getTotalRefunds() + order.getTotalSellingPrice());
		sellerReportService.updateSellerReport(report);
		return ResponseEntity.ok(order);
	}

}
