package com.nexamart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexamart.modal.Seller;
import com.nexamart.modal.Transaction;
import com.nexamart.service.SellerService;
import com.nexamart.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
	
	private final TransactionService transactionService;
	private final SellerService sellerService;
	
	@GetMapping("/seller")
	public ResponseEntity<List<Transaction>> getTransactionBySeller(@RequestHeader("Authorization") String jwt) throws Exception{
		
		Seller seller = sellerService.getSellerProfile(jwt);
		
		List<Transaction> transaction = transactionService.getTransactionBySellerId(seller);
		
		return ResponseEntity.ok(transaction);
		
	}
	
	@GetMapping
	public ResponseEntity<List<Transaction>> getAllTransaction(){
		
		List<Transaction> allTransaction = transactionService.getAllTransaction();
		
		return ResponseEntity.ok(allTransaction);
		
	}
	
	

}
