package com.nexamart.service.impl;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexamart.config.JwtProvider;
import com.nexamart.domain.AccountStatus;
import com.nexamart.domain.USER_ROLE;
import com.nexamart.modal.Address;
import com.nexamart.modal.Seller;
import com.nexamart.repository.AddressRepository;
import com.nexamart.repository.SellerRepository;
import com.nexamart.service.SellerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

	private final PasswordEncoder passwordEncoder;
	private final SellerRepository sellerRepository;
	private final JwtProvider jwtProvider;
	private final AddressRepository addressRepository;

	@Override
	public Seller getSellerProfile(String jwt) throws Exception {
		String emailFromJwtToken = jwtProvider.getEmailFromJwtToken(jwt);
		return this.getSellerByEmail(emailFromJwtToken);
	}

	@Override
	public Seller createSeller(Seller seller) throws Exception {

		Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());

		if (sellerExist != null) {
			throw new Exception("Seller already exist with provide email id, used different email");
		}

		Address saveAddress = addressRepository.save(seller.getPickUpAddress());

		Seller newSeller = new Seller();
		newSeller.setEmail(seller.getEmail());
		newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
		newSeller.setSellerName(seller.getSellerName());
		newSeller.setPickUpAddress(saveAddress);
		newSeller.setGSTIN(seller.getGSTIN());
		newSeller.setRole(USER_ROLE.ROLE_SELLER);
		newSeller.setBankDetails(seller.getBankDetails());
		newSeller.setMobile(seller.getMobile());
		newSeller.setBusinessDetails(seller.getBusinessDetails());
		
		System.out.println("Your info:"+ newSeller);
		


		return sellerRepository.save(newSeller);

	}

	@Override
	public Seller getSellerById(Long id) throws Exception {
		return sellerRepository.findById(id).orElseThrow(() -> new Exception("Seller not found with id: " + id));
	}

	@Override
	public Seller getSellerByEmail(String email) throws Exception {
		Seller seller = sellerRepository.findByEmail(email);
		if (seller == null) {
			throw new Exception("Seller not found");
		}
		return seller;
	}

	@Override
	public List<Seller> getAllSellers(AccountStatus status) {
		return sellerRepository.findByAccountStatus(status);
	}

	@Override
	public Seller updateSeller(Long id, Seller seller) throws Exception {
		Seller existingSeller = this.getSellerById(id);

		if (seller.getSellerName() != null) {
			existingSeller.setSellerName(seller.getSellerName());
		}
		if (seller.getMobile() != null) {
			existingSeller.setMobile(seller.getMobile());
		}
		if (seller.getEmail() != null) {
			existingSeller.setEmail(seller.getEmail());
		}
		if (seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null) {
			existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
		}
		if (seller.getBankDetails() != null && seller.getBankDetails().getAccountHolderName() != null
				&& seller.getBankDetails().getAccountNumber() != null
				&& seller.getBankDetails().getIfscCode() != null) {

			existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
			existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
			existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getIfscCode());

		}
		if (seller.getPickUpAddress() != null && seller.getPickUpAddress().getAddress() != null
				&& seller.getPickUpAddress().getMobile() != null && seller.getPickUpAddress().getCity() != null
				&& seller.getPickUpAddress().getState() != null) {

			existingSeller.getPickUpAddress().setAddress(seller.getPickUpAddress().getAddress());
			existingSeller.getPickUpAddress().setMobile(seller.getPickUpAddress().getMobile());
			existingSeller.getPickUpAddress().setCity(seller.getPickUpAddress().getCity());
			existingSeller.getPickUpAddress().setState(seller.getPickUpAddress().getState());
			existingSeller.getPickUpAddress().setPincode(seller.getPickUpAddress().getPincode());

		}
		if (seller.getGSTIN() != null) {
			existingSeller.setGSTIN(seller.getGSTIN());
		}

		return sellerRepository.save(existingSeller);
	}

	@Override
	public void deleteSeller(Long id) throws Exception {
		Seller existSeller = this.getSellerById(id);

		sellerRepository.delete(existSeller);
	}

	@Override
	public Seller verifyEmail(String email, String otp) throws Exception {
		Seller seller = getSellerByEmail(email);
		seller.setEmailVerified(true);
		return sellerRepository.save(seller);
	}

	@Override
	public Seller uodateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {
		Seller seller = getSellerById(sellerId);
		seller.setAccountStatus(status);
		return sellerRepository.save(seller);

	}

}
