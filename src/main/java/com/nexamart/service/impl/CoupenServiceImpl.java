package com.nexamart.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.nexamart.controller.UserController;
import com.nexamart.modal.Cart;
import com.nexamart.modal.Coupen;
import com.nexamart.modal.User;
import com.nexamart.repository.CartRepository;
import com.nexamart.repository.CoupenRepository;
import com.nexamart.repository.UserRepository;
import com.nexamart.service.CoupenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoupenServiceImpl implements CoupenService {

	private final UserController userController;

	private final UserRepository userRepository;

	private final CoupenRepository coupenRepository;
	private final CartRepository cartRepository;

	@Override
	public Cart applyCoupen(String code, double orderValue, User user) throws Exception {
		Coupen coupen = coupenRepository.findByCode(code);
		Cart cart = cartRepository.findByUserId(user.getId());

		if (coupen == null) {
			throw new Exception("Coupen not valid");
		}

		if (user.getUsedCoupons().contains(coupen)) {
			throw new Exception("Coupen all ready used");
		}

		if (orderValue < coupen.getMinimumOrderValue()) {
			throw new Exception("Valid for minimum order value: " + coupen.getMinimumOrderValue());
		}

		if (coupen.isActive() && LocalDate.now().isAfter(coupen.getValidStartDate())
				&& LocalDate.now().isBefore(coupen.getValidEndDate())) {

			user.getUsedCoupons().add(coupen);
			userRepository.save(user);

			double discountPrice = (cart.getTotalSellingPrice() * coupen.getDiscountPercentage()) / 100;

			cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountPrice);
			cart.setCouponCode(code);
			cartRepository.save(cart);

			return cart;

		}

		throw new Exception("Coupen is not valid");
	}

	@Override
	public Cart removedCoupen(String code, User user) throws Exception {
		Coupen coupen = coupenRepository.findByCode(code);

		if (coupen == null) {
			throw new Exception("Coupen not found");
		}

		Cart cart = cartRepository.findByUserId(user.getId());

		double discountPrice = (cart.getTotalSellingPrice() * coupen.getDiscountPercentage()) / 100;

		cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountPrice);

		cart.setCouponCode(null);

		return cartRepository.save(cart);
	}

	@Override
	public Coupen findCoupenById(Long id) throws Exception {
		return coupenRepository.findById(id).orElseThrow(() -> new Exception("Coupen not found"));
	}

	@Override
	@PreAuthorize("hasRole ('ADMIN')")
	public Coupen createCoupen(Coupen coupen) {

		return coupenRepository.save(coupen);
	}

	@Override
	public List<Coupen> findAllCoupen() {

		return coupenRepository.findAll();
	}

	@Override
	@PreAuthorize("hasRole ('ADMIN')")
	public void deleteCoupen(Long id) throws Exception {

		findCoupenById(id);
		coupenRepository.deleteById(id);

	}

}
