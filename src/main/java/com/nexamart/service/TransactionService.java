package com.nexamart.service;

import java.util.List;

import com.nexamart.modal.Order;
import com.nexamart.modal.Seller;
import com.nexamart.modal.Transaction;

public interface TransactionService {
	
	Transaction createTransaction(Order order);
	
	List<Transaction> getTransactionBySellerId(Seller seller);
	
	List<Transaction> getAllTransaction();
	
	

}
