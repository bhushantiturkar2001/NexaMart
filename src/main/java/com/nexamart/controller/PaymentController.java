package com.nexamart.controller;

import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexamart.modal.Order;
import com.nexamart.modal.PaymentOrder;
import com.nexamart.modal.Seller;
import com.nexamart.modal.SellerReport;
import com.nexamart.modal.User;
import com.nexamart.response.ApiResponse;
import com.nexamart.response.PaymentLinkResponse;
import com.nexamart.service.OrderService;
import com.nexamart.service.PaymentService;
import com.nexamart.service.SellerReportService;
import com.nexamart.service.SellerService;
import com.nexamart.service.TransactionService;
import com.nexamart.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

	private final PaymentService paymentService;
	private final UserService userService;
	private final SellerService sellerService;
	private final OrderService orderService;
	private final SellerReportService sellerReportService;
	private final TransactionService transactionService;

	/**
	 * @param paymentId
	 * @param paymentLinkId
	 * @param jwt
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<ApiResponse> paymentSuccessHandler(@PathVariable String paymentId,
			@RequestParam String paymentLinkId, @RequestHeader("Authorization") String jwt) throws Exception {

		User user = userService.findByJwtToken(jwt);

		PaymentLinkResponse paymentLinkResponse;

		PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

		boolean paymentSuccess = paymentService.ProceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);

		if (paymentSuccess) {

			for (Order order : paymentOrder.getOrders()) {
				
				transactionService.createTransaction(order);

				Seller seller = sellerService.getSellerById(order.getSellerId());

				SellerReport report = sellerReportService.getSellerReport(seller);

				report.setTotalOrders(report.getTotalOrders() + 1);
				report.setTotalEarnings(report.getTotalEarnings() + order.getTotalSellingPrice());
				report.setTotalSales(report.getTotalSales() + order.getOrderItems().size());
				
				sellerReportService.updateSellerReport(report);

			}
			

		}
		
		ApiResponse response = new ApiResponse();
		response.setMessage("Payment successful");
		

		return new ResponseEntity<>(response,HttpStatus.CREATED);

	}

}
