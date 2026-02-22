package com.nexamart.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nexamart.modal.Order;
import com.nexamart.modal.Seller;
import com.nexamart.modal.Transaction;
import com.nexamart.repository.SellerReportRepository;
import com.nexamart.repository.SellerRepository;
import com.nexamart.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements com.nexamart.service.TransactionService {

	private final SellerRepository sellerRepository;
	private final TransactionRepository transactionRepository;

	@Override
	public Transaction createTransaction(Order order) {
		Seller seller = sellerRepository.findById(order.getSellerId()).get();

		Transaction transaction = new Transaction();
		transaction.setSeller(seller);
		transaction.setCustomer(order.getUser());
		transaction.setOrder(order);

		return transactionRepository.save(transaction);
	}

	@Override
	public List<Transaction> getTransactionBySellerId(Seller seller) {
		
		return transactionRepository.findBySellerId(seller.getId());
	}

	@Override
	public List<Transaction> getAllTransaction() {
		
		return transactionRepository.findAll();
	}

}
