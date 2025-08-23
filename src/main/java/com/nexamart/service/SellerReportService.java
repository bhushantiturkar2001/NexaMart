package com.nexamart.service;

import com.nexamart.modal.Seller;
import com.nexamart.modal.SellerReport;

public interface SellerReportService {
	
	SellerReport getSellerReport(Seller seller);
	
	SellerReport updateSellerReport(SellerReport sellerReport);

}
