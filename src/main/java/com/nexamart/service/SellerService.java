package com.nexamart.service;

import java.util.List;

import com.nexamart.domain.AccountStatus;
import com.nexamart.exception.SellerException;
import com.nexamart.modal.Seller;

public interface SellerService {

	Seller getSellerProfile(String jwt) throws Exception;

	Seller createSeller(Seller seller) throws Exception;

	Seller getSellerById(Long id) throws SellerException;

	Seller getSellerByEmail(String email) throws Exception;

	List<Seller> getAllSellers(AccountStatus status);

	Seller updateSeller(Long id, Seller seller) throws Exception;

	void deleteSeller(Long id) throws Exception;

	Seller verifyEmail(String email, String otp) throws Exception;

	Seller uodateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception;

}
