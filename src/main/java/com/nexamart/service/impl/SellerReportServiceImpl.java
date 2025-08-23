package com.nexamart.service.impl;

import org.springframework.stereotype.Service;

import com.nexamart.modal.Seller;
import com.nexamart.modal.SellerReport;
import com.nexamart.repository.SellerReportRepository;
import com.nexamart.service.SellerReportService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {
	// For this class controller is in seller controller and order controller
	private final SellerReportRepository sellerReportRepository;  

	@Override
	public SellerReport getSellerReport(Seller seller) {
		SellerReport sr = sellerReportRepository.findBySellerId(seller.getId());
		
		if (sr==null) {
			SellerReport newReport = new SellerReport();
			newReport.setSeller(seller);
			return sellerReportRepository.save(newReport);
		}
		return sr;
	}

	@Override
	public SellerReport updateSellerReport(SellerReport sellerReport) {
		return sellerReportRepository.save(sellerReport);
	}

}
