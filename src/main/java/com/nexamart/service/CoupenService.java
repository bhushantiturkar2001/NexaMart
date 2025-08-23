package com.nexamart.service;

import java.util.List;

import com.nexamart.modal.Cart;
import com.nexamart.modal.Coupen;
import com.nexamart.modal.User;

public interface CoupenService {
	
	Cart applyCoupen(String code, double orderValue, User user) throws Exception;
	
	Cart removedCoupen(String code, User user) throws Exception;
	
	Coupen findCoupenById(Long id) throws Exception;
	
	Coupen createCoupen(Coupen coupen);
	
	List<Coupen> findAllCoupen();
	
	void deleteCoupen(Long id) throws Exception;

}
