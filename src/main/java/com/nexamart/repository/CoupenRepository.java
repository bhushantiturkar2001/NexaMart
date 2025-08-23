package com.nexamart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexamart.modal.Coupen;

public interface CoupenRepository extends JpaRepository<Coupen, Long> {
	
	Coupen findByCode(String code);

}
