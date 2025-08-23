package com.nexamart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexamart.modal.SellerReport;

public interface SellerReportRepository extends JpaRepository<SellerReport, Long> {
	
	SellerReport findBySellerId(Long sellerId);

}
