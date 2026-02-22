package com.nexamart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexamart.modal.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	List<Transaction> findBySellerId(Long sellerId);

}
