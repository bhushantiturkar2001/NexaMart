package com.nexamart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexamart.modal.Seller;
import java.util.List;


public interface SellerRepository extends JpaRepository<Seller, Long> {
	Seller findByEmail(String email);

}
