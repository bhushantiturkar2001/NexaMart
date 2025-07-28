package com.nexamart.modal;

import com.nexamart.domain.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {
	
	private String paymentId;
	private String razorpayPaymentId;
	private String razorpayPaymentReferenceId;
	private String razorpayPaymentLinkStatus;
	private String razorpayPaymentIdZWSP;
	private PaymentStatus status;

}
